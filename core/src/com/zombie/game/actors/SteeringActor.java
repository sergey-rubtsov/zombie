/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.zombie.game.actors;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.zombie.game.events.ActorHighlightedEvent;
import com.zombie.game.steering.Scene2dLocation;
import com.zombie.game.steering.Scene2dSteeringUtils;

/** A SteeringActor is a scene2d {@link Actor} implementing the {@link Steerable} interface.
 *
 * @autor davebaol */
public class SteeringActor extends Actor implements AnimationController.AnimationListener, Steerable<Vector2> {

    static public final float RIGHT_ANGLE = 3.1415927f / 2;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

    ModelInstance modelInstance;

    /** Animation controller */
    protected AnimationController animationController;

    TextureRegion region;

    Vector2 position; // like scene2d centerX and centerY, but we need a vector to implement Steerable

    Quaternion direction = new Quaternion(Vector3.Y, 0);
    Quaternion perspective = new Quaternion(Vector3.X, 0);
    float transform[] = new float[16];

    Vector2 linearVelocity;
    float angularVelocity;
    float boundingRadius;
    boolean tagged;
    boolean active = false;
    Mob mob = null;

    float maxLinearSpeed = 100;
    float maxLinearAcceleration = 200;
    float maxAngularSpeed = 5;
    float maxAngularAcceleration = 10;

    boolean independentFacing;

    SteeringBehavior<Vector2> steeringBehavior;

    public SteeringActor(TextureRegion region) {
        this(region, false);
    }

    public SteeringActor(TextureRegion region, boolean independentFacing) {
        this.independentFacing = independentFacing;
        this.region = region;
        this.position = new Vector2();
        this.linearVelocity = new Vector2();
        this.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
        this.boundingRadius = (region.getRegionWidth() + region.getRegionHeight()) / 4f;
        this.setOrigin(region.getRegionWidth() * .5f, region.getRegionHeight() * .5f);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Scene2dLocation();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Scene2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Scene2dSteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    @Override
    public void act(float delta) {
        position.set(getX(Align.center), getY(Align.center));
        if (steeringBehavior != null) {

            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

			/*
             * Here you might want to add a motor control layer filtering steering accelerations.
			 * 
			 * For modelInstance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

            // Apply steering acceleration
            applySteering(steeringOutput, delta);

            wrapAround(position, getParent().getWidth(), getParent().getHeight());
            setPosition(position.x, position.y, Align.center);
        }
        super.act(delta);
    }

    @Override
    public void setPosition (float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
    }

    public void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
        this.transform = modelInstance.transform.val;
        for(Material m : modelInstance.materials){
            m.set(new IntAttribute(IntAttribute.CullFace, GL20.GL_NONE));
            m.set(new FloatAttribute(FloatAttribute.AlphaTest, 0.5f));
            m.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        }
        //Get animations if any
        if(modelInstance.animations.size > 0){
            animationController = new AnimationController(modelInstance);
            animationController.allowSameAnimation = true;
            animationController.animate(modelInstance.animations.get(0).id, -1, 1f, this, 1f);
            //currentAnimation = 0;
            //animationController.animate(modelInstance.animations.get(currentAnimation).id, this, 0.5f);
        }
    }

    // the display area is considered to wrap around from top to bottom
    // and from left to right
    protected static void wrapAround(Vector2 pos, float maxX, float maxY) {
        if (pos.x > maxX) pos.x = 0.0f;

        if (pos.x < 0) pos.x = maxX;

        if (pos.y < 0) pos.y = maxY;

        if (pos.y > maxY) pos.y = 0.0f;
    }

    private void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        // Update position and linear velocity. Velocity is trimmed to maximum speed
        position.mulAdd(linearVelocity, time);
        linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed());

        // Update orientation and angular velocity
        if (independentFacing) {
            setRotation(getRotation() + (angularVelocity * time) * MathUtils.radiansToDegrees);
            angularVelocity += steering.angular * time;
        } else {
            // If we haven't got any velocity, then we can do nothing.
            if (!linearVelocity.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linearVelocity);
                angularVelocity = (newOrientation - getRotation() * MathUtils.degreesToRadians) * time; // this is superfluous if independentFacing is always true
                setRotation(newOrientation * MathUtils.radiansToDegrees);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
    }

    @Override
    public boolean fire(Event event) {
        if (event instanceof InputEvent) {
            InputEvent.Type t = ((InputEvent) event).getType();
            event.stop();
            ActorHighlightedEvent actorEvent = new ActorHighlightedEvent();
            actorEvent.setSteeringActor(this);
            if (t != InputEvent.Type.exit) {
                this.active = true;
                return super.fire(actorEvent);
            } this.active = false;
        }
        return super.fire(event);
    }

    public boolean isActive() {
        return active;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;
    }

    public void deselect() {
        mob = null;
    }


    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {
    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {
    }

    /** Called when the actor's position has been changed. */
    protected void positionChanged () {
        transform[Matrix4.M03] = getPosition().x;
        transform[Matrix4.M13] = getPosition().y;
    }

    /** Called when the actor's size has been changed. */
    protected void sizeChanged () {
    }

    /** Called when the actor's rotation has been changed. */
    protected void rotationChanged () {
        direction.set(Vector3.Y, getRotation() + 180);
        perspective.set(Vector3.X, 60);
        perspective.mul(direction);
        perspective.toMatrix(transform);
    }
}
