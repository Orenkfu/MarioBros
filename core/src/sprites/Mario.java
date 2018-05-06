package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import mariobros.excercise.screens.PlayScreen;
import mariobros.excercise.setup.MarioBros;

public class Mario extends Sprite {
	private TextureRegion marioStand;
	private final static int SPRITE_SIZE = 16;
	private final static int SPRITE_START_X = 0;
	private final static int SPRITE_START_Y = 0;
	private static final int RUN_IMAGES = 4;
	private static final int JUMP_IMAGE_END = 6;

	public World world;
	public Body b2body;

	private MarioState currentState;
	private MarioState previousState;

	private Animation<TextureRegion> marioRun;
	private Animation<TextureRegion> marioJump;
	private boolean runningRight;
	private float stateTimer;

	public Mario(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("little_mario"));
		this.world = world;
		defineMarioBox();
		animateMario();
		marioStand = new TextureRegion(getTexture(), SPRITE_START_X, SPRITE_START_Y, SPRITE_SIZE, SPRITE_SIZE);
		setBounds(SPRITE_START_X, SPRITE_START_Y, SPRITE_SIZE / MarioBros.PPM, SPRITE_SIZE / MarioBros.PPM);
		setRegion(marioStand);
	}

	private void animateMario() {
		currentState = MarioState.STANDING;
		previousState = MarioState.STANDING;
		stateTimer = 0;
		runningRight = true;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 1; i < RUN_IMAGES; i++) {
			frames.add(new TextureRegion(getTexture(), i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
		}
		marioRun = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		for (int i = RUN_IMAGES; i < JUMP_IMAGE_END; i++) {
			frames.add(new TextureRegion(getTexture(), i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
		}
		marioJump = new Animation<TextureRegion>(0.2f, frames);

	}

	private void defineMarioBox() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5 / MarioBros.PPM);

		fdef.shape = shape;
		b2body.createFixture(fdef);

	}

	public void update(float delta) {
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(delta));
	}

	private TextureRegion getFrame(float delta) {
		currentState = getState();

		TextureRegion region;
		switch (currentState) {
		case JUMPING:
			region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
			break;
		case RUNNING:
			region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
		case FALLING:
		case STANDING:
		default:
			region = marioStand;
			break;
		}
		if ((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		stateTimer = currentState == previousState ? stateTimer + delta : 0;
		previousState = currentState;
		return region;
	}

	private MarioState getState() {
		if (b2body.getLinearVelocity().y > 0
				|| (b2body.getLinearVelocity().y < 0 && previousState == MarioState.JUMPING))
			return MarioState.JUMPING;
		else if (b2body.getLinearVelocity().y < 0)
			return MarioState.FALLING;
		else if (b2body.getLinearVelocity().x != 0)
			return MarioState.RUNNING;
		else

			return MarioState.STANDING;
	}
}
