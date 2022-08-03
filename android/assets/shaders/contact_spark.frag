#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
uniform vec2 u_resolution;
uniform float u_time;
uniform sampler2D u_sampler2D;
uniform vec2 u_world2screen;
uniform vec4 u_bbox;
uniform float u_angle;
varying vec2 v_texCoord0;

vec4 makeGlow(vec2 v) {
    float d = abs(v.y - 0.5);
    vec4 col = vec4(1., 1., 1., 1.);
    float power = 3.;
    float glow = power * 0.01/d; // create glow and diminish it with distance
    glow = clamp(glow, 0., 1.); // remove artifacts
    col.w = glow * power; // add glow
    return col;
}

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

void main(){
    mat3 rot = makeRotation(u_angle);
    vec2 uv = (gl_FragCoord.xy) / u_resolution;
    vec2 boxPos = normalizeCoordinates(u_bbox.xy);
    vec2 boxDim = normalizeCoordinates(u_bbox.zw);
    vec2 localuv = toLocalCoords(uv.xy, boxPos, boxDim);
    vec3 rotateduv = vec3(localuv, 0.) * rot;
    vec3 translateduv = rotateduv + vec3(0., 0.5, 0.);
//  vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;

  vec4 col = makeGlow(translateduv.xy);
  vec4 color = vec4(col);
  gl_FragColor = vec4(color);
}
