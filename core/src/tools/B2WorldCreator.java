package tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import mariobros.excercise.setup.MarioBros;
import sprites.Brick;
import sprites.Coin;

public class B2WorldCreator {

	private final static int GROUND_LAYER = 2;
	private final static int BRICK_LAYER = 5;
	private final static int COIN_LAYER = 4;
	private final static int PIPE_LAYER = 3;

	public B2WorldCreator(World world, TiledMap map) {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		// create ground body & fixture
		for (MapObject object : map.getLayers().get(GROUND_LAYER).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle bounds = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM,
					(bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM);
			body = world.createBody(bdef);
			shape.setAsBox((bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getHeight() / 2) / MarioBros.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		// create pipe bodies & fixtures
		for (MapObject object : map.getLayers().get(PIPE_LAYER).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle bounds = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM,
					(bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM);
			body = world.createBody(bdef);
			shape.setAsBox((bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getHeight() / 2) / MarioBros.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		// create bricks bodies & fixtures
		for (MapObject object : map.getLayers().get(BRICK_LAYER).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle bounds = ((RectangleMapObject) object).getRectangle();
			new Brick(world, map, bounds);
		}
		// create coins bodies & fixtures
		for (MapObject object : map.getLayers().get(COIN_LAYER).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle bounds = ((RectangleMapObject) object).getRectangle();
			new Coin(world, map, bounds);
		}
	}
}
