package render;

import org.lwjgl.opengl.GL20;

public class Shader {
    private final int programId;

    public Shader(String vertexCode, String fragmentCode) {
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, vertexCode);
        GL20.glCompileShader(vertexShader);
        if (GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Vertex Shader Error: " + GL20.glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShader, fragmentCode);
        GL20.glCompileShader(fragmentShader);
        if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Fragment Shader Error: " + GL20.glGetShaderInfoLog(fragmentShader));
        }

        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader);
        GL20.glAttachShader(programId, fragmentShader);
        GL20.glLinkProgram(programId);
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Shader Linking Error: " + GL20.glGetProgramInfoLog(programId));
        }

        // shaders can be deleted after linking
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }

    public void use() {
        GL20.glUseProgram(programId);
    }

    public void destroy() {
        GL20.glDeleteProgram(programId);
    }
}
