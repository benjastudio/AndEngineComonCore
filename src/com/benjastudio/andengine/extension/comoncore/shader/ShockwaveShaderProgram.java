package com.benjastudio.andengine.extension.comoncore.shader;

import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class ShockwaveShaderProgram extends ShaderProgram {

	private static ShockwaveShaderProgram instance;

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformCenterLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTimeLocation = ShaderProgramConstants.LOCATION_INVALID;

	public static ShockwaveShaderProgram getInstance() {
		if (instance == null)
			instance = new ShockwaveShaderProgram();
		return instance;
	}

	private ShockwaveShaderProgram() {
		super(PositionTextureCoordinatesShaderProgram.VERTEXSHADER,
				FRAGMENTSHADER);
	}

	public static final String FRAGMENTSHADER = "precision lowp float;\n" +

	"uniform lowp sampler2D "
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0
			+ ";\n"
			+ "varying mediump vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ";\n"
			+

			"uniform vec2 center;\n"
			+ "uniform float time;\n"
			+ "const vec3 params = vec3(10.0, 0.25, 0.1);\n"
			+

			"void main()	\n"
			+ "{				\n"
			+ "	mediump vec2 texCoord = "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ";\n"
			+ "	float distance = distance(texCoord, center);\n"
			+ "	if ( (distance <= (time*0.5 + params.z)) && (distance >= (time*0.5 - params.z)) )\n"
			+ "	{\n"
			+ "		float diff = (distance - time*0.5);\n"
			+
			/*
			 * "		float powDiff = 1.0 - pow(abs(diff*params.x), params.y);\n" +
			 * "		float diffTime = diff  * powDiff;\n" +
			 * "		vec2 diffUV = normalize(texCoord - center);\n" +
			 * "		texCoord = texCoord + (diffUV * diffTime);\n" +
			 */
			"		float powDiff = 1.0 - pow(abs(diff*params.x), params.y);\n"
			+ "		texCoord = texCoord + (normalize(texCoord - center) * (diff * powDiff));\n"
			+ "	}\n" + "	gl_FragColor = texture2D("
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", texCoord);\n"
			+ "}		\n";

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

		ShockwaveShaderProgram.sUniformModelViewPositionMatrixLocation = this
				.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		ShockwaveShaderProgram.sUniformTexture0Location = this
				.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);

		ShockwaveShaderProgram.sUniformCenterLocation = this
				.getUniformLocation("center");
		ShockwaveShaderProgram.sUniformTimeLocation = this
				.getUniformLocation("time");
	}

	@Override
	public void bind(final GLState pGLState,
			final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(
				ShockwaveShaderProgram.sUniformModelViewPositionMatrixLocation,
				1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(ShockwaveShaderProgram.sUniformTexture0Location, 0);
	}

	@Override
	public void unbind(final GLState pGLState) throws ShaderProgramException {
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.unbind(pGLState);
	}
}