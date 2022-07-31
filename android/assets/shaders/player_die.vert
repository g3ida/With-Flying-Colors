#ifdef GL_ES
precision mediump float;
#endif

attribute vec4 a_color;
attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_resolution;
uniform float u_time;
uniform vec4 u_player_pos;
uniform vec2 u_campos;
uniform vec2 u_contactpos;
uniform vec2 u_world2screen;
uniform vec4 u_bbox;
uniform float u_angle;
uniform float u_animation_timer;
uniform float u_animation_duration;

varying vec4 v_color;
varying vec2 v_texCoord0;

float strength = .2;
float PI = 3.14159265359;

mat3 makeTranslation(vec2 t) {
    mat3 m = mat3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, t.x, t.y, 1.0);
    return m;
}

mat3 makeRotation( float angleInRadians ){
    float c = cos(angleInRadians);
    float s = sin(angleInRadians);
    mat3 m = mat3(c, -s, 0, s, c, 0, 0, 0, 1);
    return m;
}

mat3 makeScale(vec2 s) {
   mat3 m = mat3( s.x, 0, 0, 0, s.y, 0, 0, 0, 1);
    return m;
}

vec2 toLocalCoords(vec2 v, vec2 xy, vec2 wh) {
    return (v - xy) / wh;
}

vec2 undoLocalCoords(vec2 v, vec2 xy, vec2 wh) {
    return v*wh + xy;
}

vec2 scaleVec(vec2 uv, vec2 center, float strength) {
    
    float dist = distance(uv , center);
    vec2 dir = normalize(uv - center);
    float scaleX = 1. + 2.*strength;
    float scaleY = 1. - strength;
    vec2 scale = vec2(scaleX, scaleY);
    return center + dist * scale * dir;
}

void main() {
    float animationStep = sin(u_animation_timer * PI * 0.5 / u_animation_duration);

    v_color = a_color;
    v_texCoord0 = a_texCoord0;
    vec2 boxPos = u_bbox.xy;
    vec2 boxDim = u_bbox.zw;
    vec2 localuv = toLocalCoords(a_position.xy, u_player_pos.xy, u_player_pos.zw);
    vec2 tmpRes = scaleVec(localuv, vec2(.5,.0), strength * animationStep);
    vec3 res = vec3(undoLocalCoords(tmpRes, u_player_pos.xy, u_player_pos.zw), 0.);
    gl_Position = u_projTrans * vec4(res, 1.);
}
