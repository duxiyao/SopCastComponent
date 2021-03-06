#extension GL_OES_EGL_image_external : require
precision mediump float;

uniform sampler2D u_Texture;

varying lowp vec2 frag_TexCoord;

void main() {
    gl_FragColor = texture2D(u_Texture, frag_TexCoord);
}
