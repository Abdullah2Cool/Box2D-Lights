package scratch.box2dlights;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import static com.badlogic.gdx.Gdx.input;

public class GamMain extends ApplicationAdapter implements InputProcessor {
    RayHandler rayHandler;
    World world;
    Body Player;
    Box2DDebugRenderer b2dr;
    OrthographicCamera camera;
    PointLight light;
    ConeLight cLight;
    int nSpeed, nSpacing;

    @Override
    public void create() {
        input.setInputProcessor(this);
        world = new World(new Vector2(0, 0f), false);
        rayHandler = new RayHandler(world);
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2, 0);
        nSpeed = 100;
        nSpacing = 400;
        Player = createBody(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), 32, 32, false);
        createBody(new Vector2(Gdx.graphics.getWidth() / 2 + nSpacing, Gdx.graphics.getHeight() / 2), 32, 100, true);
        createBody(new Vector2(Gdx.graphics.getWidth() / 2 - nSpacing, Gdx.graphics.getHeight() / 2), 32, 100, true);
        createBody(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + nSpacing / 2), 100, 32, true);
        createBody(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - nSpacing / 2), 100, 32, true);

        rayHandler.setAmbientLight(0.5f);
//        light = new PointLight(rayHandler, 200, Color.WHITE, 1000, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        cLight = new ConeLight(rayHandler, 200, Color.WHITE, 500, 0, 0, Player.getAngle(), 50);
        cLight.setSoftnessLength(0f);
        cLight.attachToBody(Player);

    }

    @Override
    public void render() {
        world.step(1 / 60f, 6, 2);
        camera.update();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, camera.combined);
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth, camera.viewportHeight);
        rayHandler.updateAndRender();
    }

    @Override
    public void dispose() {
    }

    private Body createBody(Vector2 vPos, float fWidth, float fHeight, boolean bStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (bStatic) {
            def.type = BodyDef.BodyType.StaticBody;

        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.fixedRotation = false;
        def.position.set(vPos.x, vPos.y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(fWidth, fHeight);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        pBody = world.createBody(def);
        pBody.createFixture(fixDef);
        shape.dispose();
        return pBody;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            Player.setLinearVelocity(nSpeed, 0);
        } else if (keycode == Input.Keys.LEFT) {
            Player.setLinearVelocity(-nSpeed, 0);
        } else if (keycode == Input.Keys.UP) {
            Player.setLinearVelocity(0, nSpeed);
        } else if (keycode == Input.Keys.DOWN) {
            Player.setLinearVelocity(0, -nSpeed);
        } else {
            Player.setLinearVelocity(0, 0);
        }
        if (keycode == Input.Keys.SPACE) {
            cLight.setSoftnessLength(cLight.getSoftShadowLength() + 50f);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
