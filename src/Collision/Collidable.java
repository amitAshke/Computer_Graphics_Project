package Collision;

import LinearAlgebra.Vectors.Vector3D;

public interface Collidable {

    Hitbox getProjectileCollisionShape();

    Hitbox getPlayerCollisionShape();

    Vector3D handlePlayerCollision(Vector3D newPosition);

    void projectileCollisionEffect();
}
