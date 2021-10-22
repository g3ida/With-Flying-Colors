package com.g3ida.withflyingcolours.core.player;

public class PlayerControllerSettings {

    public float responsiveness; //how much do we delay action hoping its conditions to be met.
    public float permissiveness; //how much time we permit action since its conditions were met.

    public PlayerControllerSettings(float responsiveness, float permissiveness) {
        this.responsiveness = responsiveness;
        this.permissiveness = permissiveness;
    }
}
