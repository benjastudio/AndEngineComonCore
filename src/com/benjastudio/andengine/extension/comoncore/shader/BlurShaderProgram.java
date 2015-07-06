package com.benjastudio.andengine.extension.comoncore.shader;

import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class BlurShaderProgram extends ShaderProgram {

	private static BlurShaderProgram instance;

	public static BlurShaderProgram getInstance() {
		if (instance == null)
			instance = new BlurShaderProgram();
		return instance;
	}

	public static final String FRAGMENTSHADER = "precision lowp float;\n" +

	"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n"
			+ "varying mediump vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +

			"const float blurSize = 1.8/" + (799) + ".0;	\n" +

			"void main()	\n" + "{				\n" + "	vec4 sum = vec4(0.0);	\n"
			+ "	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0
			+ ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".x - 3.0*blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.1; \n" + "	sum += texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".x - 2.0*blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.13; \n" + "	sum += texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".x - blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.17; \n" + "	sum += texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.2; \n" + "	sum += texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".x + blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.17; \n" + "	sum += texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".x + 2.0*blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.13;	\n" + "	sum += texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".x + 3.0*blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ".y)) * 0.1;	\n" + "	gl_FragColor = sum;	\n" + "}						\n";

	private BlurShaderProgram() {
		super(PositionTextureCoordinatesShaderProgram.VERTEXSHADER,
				FRAGMENTSHADER);
	}

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;

	@Override
	protected void link(final GLState pGLState)
			throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(this.mProgramID,
				ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(this.mProgramID,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		BlurShaderProgram.sUniformModelViewPositionMatrixLocation = this
				.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		BlurShaderProgram.sUniformTexture0Location = this
				.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);

	}

	@Override
	public void bind(final GLState pGLState,
			final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(
				BlurShaderProgram.sUniformModelViewPositionMatrixLocation, 1,
				false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(BlurShaderProgram.sUniformTexture0Location, 0);
	}

	@Override
	public void unbind(final GLState pGLState) throws ShaderProgramException {
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.unbind(pGLState);
	}
}
