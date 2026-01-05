package render.obj;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class Window {

    private static Window instance;

    private long handle;
    private int width;
    private int height;
    private String title;

    private Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public static void initialize(int width, int height, String title) {
        if (instance != null) {
            throw new IllegalStateException("Window already initialized");
        }
        instance = new Window(width, height, title);
        instance.init();
    }

    public static Window getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Window not initialized");
        }
        return instance;
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwShowWindow(handle);
        GL.createCapabilities();
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        instance = null;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public long getHandle() {
        return handle;
    }
}
