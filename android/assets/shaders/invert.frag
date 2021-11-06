#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
uniform vec2 u_resolution;
uniform float u_time;
uniform float start_time;
uniform sampler2D u_sampler2D;
varying vec2 v_texCoord0;

uniform vec2 u_campos;
//uniform vec2 u_playerpos;
uniform vec2 u_contactpos;

float circleshape(vec2 position, float radius){
  return step(radius, length(position - vec2(0.5)));
}

float circleshape2(vec2 position, float radius){
  //vec2 squarePos = u_playerpos / u_resolution;
  vec2 contactPos = (u_contactpos / u_resolution);
  return step(radius, length(position - contactPos));
}


void main(){

    
  vec2 position = (gl_FragCoord.xy) / u_resolution;

  vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;
    
  float circle = circleshape2(position, 1.1 * (u_time - start_time));
    
  if (circle < 0.5) {
      circle = 0.78 + min(1.0, 0.7*(u_time - start_time));
  }
    
  color = vec4(color.xyz * circle , color.w);

  gl_FragColor = vec4(color);
}
