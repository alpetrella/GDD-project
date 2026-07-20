package com.gdd.game.engine;

import com.gdd.game.engine.components.LiquidFunComponent;
import com.gdd.game.engine.components.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.Random;

public class PhysicsSystem {

    // Parameters for world simulation
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    // Particles
    private static final int MAXPARTICLECOUNT = 1000;
    private static final float PARTICLE_RADIUS = 0.3f;

    // Parameters
    private static final float DENSITY     = 1.0f;
    private static final float FRICTION    = 0.3f;
    private static final float RESTITUTION = 0.2f;
    private static final float RADIUS = 0.1f;

    // ANT
    public static final float SPEED = 0.5f;
    public static final float MAX_STEERING_ANGLE = 1.0f;

    // Physics Simulation
    public World world;
    public ParticleSystem particleSystem;
    private static final Random rng = new Random();


    /*
     * Constructor.
     */
    public PhysicsSystem() {
        this.world = new World(0, 0);  // gravity vector
    }


    // ------------------------------------------------------------------
    // Getter / Setter
    // ------------------------------------------------------------------

    public synchronized void setGravity(float x, float y)
    {
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

    public PhysicsComponent createComponent(float x, float y, float direction) {

        BodyDef bdef = new BodyDef();
        bdef.setType(BodyType.dynamicBody);
        bdef.setPosition(x, y); // spawned on the Nest
        bdef.setAngle(direction);
        bdef.setAngularDamping(0);
        bdef.setLinearDamping(0);
        bdef.setFixedRotation(true);

        var body = world.createBody(bdef);
        body.setSleepingAllowed(false);

        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS);

        FixtureDef fdef = new FixtureDef();
        fdef.setShape(shape);
        fdef.setDensity(DENSITY);
        fdef.setFriction(FRICTION);
        fdef.setRestitution(RESTITUTION);
        body.createFixture(fdef);
        var vec = new Vec2(
                SPEED * (float) Math.cos(direction),
                SPEED * (float) Math.sin(direction)
        );
        body.setLinearVelocity(vec);
        vec.delete();

        fdef.delete();
        bdef.delete();
        shape.delete();

        return new LiquidFunComponent(body);
    }


}
