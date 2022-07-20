package com.g3ida.withflyingcolours.core.ecs.systems

class PlayerControllerSettings(//how much do we delay action hoping its conditions to be met.
        var responsiveness: Float, //how much time we permit action since its conditions were met.
        var permissiveness: Float)