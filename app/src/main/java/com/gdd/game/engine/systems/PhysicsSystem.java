package com.gdd.game.engine.systems;

import com.gdd.game.engine.PhysicsBodyDef;
import com.gdd.game.engine.components.ComponentType;
import com.gdd.game.engine.components.PhysicsComponent;
import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.factories.IPhysicsFactory;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;

import java.util.List;

public class PhysicsSystem implements IPhysicsFactory {

    // Parameters for world simulation
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    // Parameters
    private static final float DENSITY     = 1.0f;
    private static final float FRICTION    = 0.3f;
    private static final float RESTITUTION = 0.2f;
    private static final float RADIUS = 0.1f;

    // Physics Simulation
    private final World world;

    private final float worldWidth, worldHeight; // boundaries


    /*
     * Constructor.
     */
    public PhysicsSystem(float worldWidth, float worldHeight) {
        this.world = new World(0, 0);  // gravity vector
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        addWorldBoundaries();
    }


    // ------------------------------------------------------------------
    // Getter / Setter
    // ------------------------------------------------------------------

    public synchronized void setGravity(float x, float y) {
        world.setGravity(x, y);
    }


    // ------------------------------------------------------------------
    // Game Loop
    // ------------------------------------------------------------------

    public synchronized void step(float dt) {
        // Handle collisions: advance the physics simulation
        world.step(dt, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }


    // ------------------------------------------------------------------
    // Component
    // ------------------------------------------------------------------

    /*
      Informazioni da impostare:

      tipo: static, dynamic, cinematic
      posizione (x,y)
      direzione
      shape = circle (+ radius) or polygon (+ size)
     */
    @Override
    public PhysicsComponent createComponent(PhysicsBodyDef def) {

        if(def == null)
            return null;

        // BODY DEF
        BodyDef bdef = new BodyDef();
        bdef.setType( def.bodyType );
        bdef.setPosition( def.x, def.y);
        bdef.setAngle( def.direction );
        bdef.setAngularDamping(0);
        bdef.setLinearDamping(0);
        bdef.setFixedRotation(true);

        // BODY
        var body = world.createBody(bdef);
        body.setSleepingAllowed(false);

        // SHAPE
        Shape shape;
        if(def.shapeType == PhysicsBodyDef.ShapeType.CIRCLE) {
            CircleShape circle = new CircleShape();
            circle.setRadius( def.radius );
            shape = circle;
        } else {
            PolygonShape polygon = new PolygonShape();
            polygon.setAsBox( def.width/2, def.height/2 );
            shape = polygon;
        }

        // FIXTURE DEF
        FixtureDef fdef = new FixtureDef();
        fdef.setShape(shape);
        fdef.setDensity(DENSITY);
        fdef.setFriction(FRICTION);
        fdef.setRestitution(RESTITUTION);
        body.createFixture(fdef);

        shape.delete();
        fdef.delete();
        bdef.delete();

        return new PhysicsComponent(body);
    }


    // ------------------------------------------------------------------
    // Utils
    // ------------------------------------------------------------------

    private void addWorldBoundaries() {

        float THICKNESS = 1f;
        float xmax = worldWidth / 2;
        float xmin = -xmax;
        float ymax = worldHeight / 2;
        float ymin = -ymax;

        // body definition: position and type
        BodyDef bdef = new BodyDef();

        var body = world.createBody(bdef);
        body.setSleepingAllowed(false);

        PolygonShape shape = new PolygonShape();

        FixtureDef fdef = new FixtureDef();
        fdef.setShape(shape);
        fdef.setDensity(0.f);
        fdef.setFriction(0.f);
        fdef.setRestitution(0.8f);

        // top
        shape.setAsBox(xmax-xmin, THICKNESS, xmin+(xmax-xmin)/2, ymin, 0);
        body.createFixture(fdef);
        // bottom
        shape.setAsBox(xmax-xmin, THICKNESS, xmin+(xmax-xmin)/2, ymax, 0);
        body.createFixture(fdef);
        // left
        shape.setAsBox(THICKNESS, ymax-ymin, xmin, ymin+(ymax-ymin)/2, 0);
        body.createFixture(fdef);
        // right
        shape.setAsBox(THICKNESS, ymax-ymin, xmax, ymin+(ymax-ymin)/2, 0);
        body.createFixture(fdef);

        // clean up native objects
        bdef.delete();
        shape.delete();
        fdef.delete();
    }


    /*
     * Sync transform and physics
     */
    public void sync(List<Actor> actors) {

        int n = actors.size();
        for(int i=0; i<n; i++)  {

            Actor a = actors.get(i);
            PhysicsComponent pc = (PhysicsComponent) a.getComponent(ComponentType.PHYSICS);
            if(pc != null) {
                pc.syncTransform();
            }
        }
    }

}
