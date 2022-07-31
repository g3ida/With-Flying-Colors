#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;
uniform vec2 u_resolution;
uniform float u_time;
uniform sampler2D u_sampler2D;
uniform vec4 u_player_pos;
uniform vec2 u_campos;
uniform vec2 u_contactpos;
uniform vec2 u_world2screen;
uniform vec4 u_bbox;
uniform float u_angle;
uniform float u_animation_timer;
uniform float u_animation_duration;

float strength = .2;
float PI = 3.14159265359;

vec2 normalizeCoordinates(vec2 v) {
    return (v * u_world2screen*2.) / u_resolution;
}

vec2 toLocalCoords(vec2 v, vec2 xy, vec2 wh) {
    return (v - xy) / wh;
}

vec2 undoLocalCoords(vec2 v, vec2 xy, vec2 wh) {
    return v*wh + xy;
}

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

mat3 transpose(mat3 m) {
  return mat3(m[0][0], m[1][0], m[2][0],
              m[0][1], m[1][1], m[2][1],
              m[0][2], m[1][2], m[2][2]);
}

void main() {
    float animationStep = sin(u_animation_timer * PI * 0.5 / u_animation_duration);
    
    vec2 uv = (gl_FragCoord.xy / u_resolution);
    vec2 boxPos = normalizeCoordinates(u_bbox.xy);
    vec2 boxDim = normalizeCoordinates(u_bbox.zw);
    boxPos.x = boxPos.x + boxDim.x * 0.5;
    boxDim.x = boxDim.x * (1. + 2.*strength * animationStep);
    boxPos.x = boxPos.x - boxDim.x * 0.5;
    boxDim.y = boxDim.y * (1. - strength * animationStep);
    vec2 localuv = toLocalCoords(uv, boxPos, boxDim);
        
    float dist = abs(localuv.y - .5);
    float factor = dist*dist*animationStep;
    float scale = 1. + factor;
    float trans = factor*boxDim.x; //set back to view coords.
    
    mat3 rotMat = makeRotation(u_angle);
    mat3 invRotMat = makeRotation(-u_angle);
    mat3 scaleMat = makeScale(vec2(scale, 1.));
    
    float transFactor = .5;
    vec3 transVec = vec3(transFactor*(sin(u_angle)+cos(u_angle)), 0., 0.);

    vec3 rotated = vec3(v_texCoord0.xy, 0.) * invRotMat;
    vec3 tmp = rotated - transVec;
    vec3 scaled = tmp * scaleMat;
    vec3 translated = scaled + transVec;
    vec2 tex = (translated * rotMat).xy;

    vec4 color = texture2D(u_sampler2D, tex)* v_color;

    // removes adjescent garbage in the texture.
    // uncomment if you have adjescent texture in the atlas.

    //vec3 rotatedLocaluv = vec3(localuv, 0.) * invRotMat;
    //vec3 rotatedCenter = vec3(.5, localuv.y, 0.) * invRotMat;
    //if ( distance(rotatedCenter.xy, rotatedLocaluv.xy) > .5 - factor/2.)
    //    color = vec4(0., 0., 0., 0.);
    
    gl_FragColor = color;
}
