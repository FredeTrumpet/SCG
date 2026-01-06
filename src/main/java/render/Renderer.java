package render;
import objectHierarchy.GameObj;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryUtil;
import render.obj.Camera;
import render.obj.Window;

public class Renderer {
    private static Renderer instance;
    private int vaoId, vboId, eboId;
    private Shader shader;
    private final Map<String, Integer> gameObjTextures = new HashMap<>();


    private Window window;

    private Renderer() {}

    // Call ONCE
    public static void initialize() {
        if (instance != null) {
            throw new IllegalStateException("Renderer already initialized");
        }
        instance = new Renderer();
        instance.init();
    }

    public static Renderer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Renderer not initialized");
        }
        return instance;
    }




    public void setWindow() {
        this.window = Window.getInstance();
    }

    public void init() {
        // Vertex positions + UVs (x, y, u, v)
        float[] vertices = {
                // positions     // UVs
                0.0f, 0.0f,      0.0f, 0.0f,
                1.0f, 0.0f,      1.0f, 0.0f,
                1.0f, 1.0f,      1.0f, 1.0f,
                0.0f, 1.0f,      0.0f, 1.0f
        };

        int[] indices = { 0, 1, 2, 2, 3, 0 }; // 2 triangles

        // --- VAO ---
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // --- VBO ---
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(vertexBuffer);

        // --- EBO ---
        eboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(indices).flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(indexBuffer);

        // --- Vertex Attributes ---
        // position
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        // UV
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // --- Shaders ---
        String vertexShader = """
                #version 330 core
                   layout(location = 0) in vec2 position;
                   layout(location = 1) in vec2 texCoord;
                
                   out vec2 vTexCoord;
                   uniform vec2 offset;
                   uniform vec2 scale;
                   uniform vec2 screenSize;
                
                   void main() {
                       // convert position in pixels -> NDC
                       vec2 ndcOffset = vec2(
                           (offset.x / screenSize.x) * 2.0 - 1.0,
                           1.0 - (offset.y / screenSize.y) * 2.0  // flip Y
                       );
                       vec2 ndcScale = (position * scale) / screenSize * 2.0;
                       gl_Position = vec4(ndcOffset + ndcScale, 0.0, 1.0);
                       vTexCoord = texCoord;
                   }
                
                
                """;

        String fragmentShader = """
                #version 330 core
                in vec2 vTexCoord;
                out vec4 fragColor;

                uniform sampler2D spriteTexture;

                void main() {
                    fragColor = texture(spriteTexture, vTexCoord);
                }
                """;

        shader = new Shader(vertexShader, fragmentShader);
        // ✅ Enable transparency blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // ✅ (Optional) Disable depth test for 2D
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }



    public void addTextures(String name, String path) {
        int texId = loadTexture(path);
        gameObjTextures.put(name, texId);
    }

    public void renderGameObj(GameObj gameObj){
        shader.use();

        int screenLoc = GL20.glGetUniformLocation(getShaderProgramId(), "screenSize");
        GL20.glUniform2f(screenLoc, Window.getInstance().getSize().width(), Window.getInstance().getSize().height());

        int offsetLoc = GL20.glGetUniformLocation(getShaderProgramId(), "offset");
        GL20.glUniform2f(offsetLoc, gameObj.getSprite().bottomLeftCorner().x() - Camera.getInstance().getX(), gameObj.getSprite().bottomLeftCorner().y() - Camera.getInstance().getY());

        int scaleLoc = GL20.glGetUniformLocation(getShaderProgramId(), "scale");
        GL20.glUniform2f(scaleLoc, gameObj.getSprite().size().width(), gameObj.getSprite().size().height());

        String texKey = gameObj.getSprite().id();
        Integer texId = null;
        texId = gameObjTextures.get(texKey);

        if (texId == null) {
            System.err.println("This texture was not found " + texKey);
            return;
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);


        int texLoc = GL20.glGetUniformLocation(getShaderProgramId(), "spriteTexture");
        GL20.glUniform1i(texLoc, 0);

        GL30.glBindVertexArray(vaoId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);

    }


    // Load PNG/JPG into OpenGL texture
    private int loadTexture(String path) {
        int texId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // texture params
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // load image
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = STBImage.stbi_load(path, w, h, comp, 4);
            if (image == null) throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(), h.get(), 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
            STBImage.stbi_image_free(image);
        }

        return texId;
    }

    private int getShaderProgramId() {
        try {
            var f = Shader.class.getDeclaredField("programId");
            f.setAccessible(true);
            return f.getInt(shader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        shader.destroy();
        GL15.glDeleteBuffers(vboId);
        GL15.glDeleteBuffers(eboId);
        GL30.glDeleteVertexArrays(vaoId);
        for (int texId : gameObjTextures.values()) {
            GL11.glDeleteTextures(texId);
        }
        gameObjTextures.clear();
    }
}
