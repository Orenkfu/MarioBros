package mariobros.excercise.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import mariobros.excercise.setup.MarioBros;
import scenes.Hud;
import sprites.Mario;
import tools.B2WorldCreator;

public class PlayScreen implements Screen {
	private MarioBros game;
	private TextureAtlas atlas;

	// follow mario cam
	private OrthographicCamera gameCam;

	private Viewport gamePort;
	private Hud hud;

	// Box2D variables
	private World world;
	private Box2DDebugRenderer b2dr;

	// Tiled map variables
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	// playscreen's mario variable
	private Mario player;

	public PlayScreen(MarioBros game) {
		atlas = new TextureAtlas("Mario_and_Enemies2.pack");

		this.game = game;
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
		gamePort.apply();

		hud = new Hud(game.batch);
		maploader = new TmxMapLoader();
		map = maploader.load("level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, (1 / MarioBros.PPM));
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		// first variable: gravity, second: "sleep" object whose not active (dont
		// physically calculate)
		world = new World(new Vector2(0, -10), true);
		b2dr = new Box2DDebugRenderer();
		player = new Mario(world, this);
		new B2WorldCreator(world, map);
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public void update(float delta) {
		handleInput(delta);
		player.update(delta);
		world.step(1 / 60f, 6, 2);
		gameCam.position.x = player.b2body.getPosition().x;
		gameCam.update();
		renderer.setView(gameCam);
	}

	private void handleInput(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
			player.b2body.applyLinearImpulse(new Vector2(0, 04f), player.b2body.getWorldCenter(), true);

		if (Gdx.input.isKeyPressed(Keys.D) && player.b2body.getLinearVelocity().x <= 2) {
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
		}
		if (Gdx.input.isKeyPressed(Keys.A) && player.b2body.getLinearVelocity().x >= -2) {
			player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
		}

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tile map renderer (world render)
		renderer.render();

		// box2d renderer
		// b2dr.render(world, gameCam.combined);
		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}

}
