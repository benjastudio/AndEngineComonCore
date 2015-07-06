package com.benjastudio.andengine.extension.comoncore.shader;

import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class TransitionShaderProgram extends ShaderProgram {

	private static TransitionShaderProgram instance;

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformCenterLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformParamsLocation = ShaderProgramConstants.LOCATION_INVALID;

	public static TransitionShaderProgram getInstance() {
		if (instance == null)
			instance = new TransitionShaderProgram();
		return instance;
	}

	private TransitionShaderProgram() {
		super(PositionTextureCoordinatesShaderProgram.VERTEXSHADER,
				FRAGMENTSHADER);
	}

	public static final String FRAGMENTSHADER = "#define M_PI 3.14159265358979323846264\n"
			+ "#define TIME params[0]\n"
			+ "#define DURATION params[1]\n"
			+ "precision mediump float;\n" +

			"uniform lowp sampler2D "
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0
			+ ";\n"
			+ "varying mediump vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ";\n"
			+

			"uniform vec2 center;\n"
			+ "uniform vec2 params;\n"
			+ // time, duration

			"void main()\n"
			+ "{\n"
			+ "	float distance = distance("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ", center);\n"
			+ "   float distanceShockwave = 1.0-TIME/DURATION;\n"
			+

			"   vec2 foo = vec2(0.0, 0.0);\n"
			+ "	vec2 direction = center - "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ";\n"
			+ "   float Din = distance*distanceShockwave;\n"
			+ "   float Dout = sin(M_PI+Din);\n"
			+ "   foo = (distanceShockwave + Dout*0.5) * direction;\n"
			+ "   gl_FragColor = texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0
			+ ", "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ " + foo);\n"
			+ "}\n";

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

		TransitionShaderProgram.sUniformModelViewPositionMatrixLocation = this
				.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		TransitionShaderProgram.sUniformTexture0Location = this
				.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);

		TransitionShaderProgram.sUniformCenterLocation = this
				.getUniformLocation("center");
		TransitionShaderProgram.sUniformParamsLocation = this
				.getUniformLocation("params");
	}

	@Override
	public void bind(final GLState pGLState,
			final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(
				TransitionShaderProgram.sUniformModelViewPositionMatrixLocation,
				1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(TransitionShaderProgram.sUniformTexture0Location, 0);
	}

	@Override
	public void unbind(final GLState pGLState) throws ShaderProgramException {
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.unbind(pGLState);
	}
}