package render.obj;

import entity.RectangleSize;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class Window {

    private static Window instance;

    private long handle;
    private RectangleSize size;
    private String title;

    private Window() {
        size = new RectangleSize(1280, 720);
        this.title = "THE GAME, PUT AT TOP OF WINDOWS WHEN BORDERLESS";
    }

    public static void initialize() {
        if (instance != null) {
            throw new IllegalStateException("Window already initialized");
        }
        instance = new Window();
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

        handle = GLFW.glfwCreateWindow((int)size.width(),(int)size.height(), title, 0, 0);
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

    public RectangleSize getSize() {
        return size;
    }

    public long getHandle() {
        return handle;
    }
}
