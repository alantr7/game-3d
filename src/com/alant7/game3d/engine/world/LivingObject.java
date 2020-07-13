package com.alant7.game3d.engine.world;

public abstract class LivingObject extends GameObject {

    private int Health, MaximumHealth;

    public void TakeDamage(int Damage) {
        this.OnDamageTaken(Damage);

        if (Health - Damage <= 0) {
            OnHealthZero();
            Health = 0;
        } else Health -= Damage;

    }

    public abstract void OnDamageTaken(int Damage);
    public abstract void OnHealthZero();

    public void SetHealth(int Health) {
        this.Health = Health;
    }
    public int GetHealth() {
        return Health;
    }

    public void SetMaximumHealth(int Health) {
        MaximumHealth = Health;
    }
    public int GetMaximumHealth() {
        return MaximumHealth;
    }

}
