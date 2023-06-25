import Engine.*;
import Engine.Object;
import org.joml.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window = new Window(1080, 1080, "Pac Man");
    ArrayList<Object> objectObj = new ArrayList<>();
    ArrayList<Object> objectGround = new ArrayList<>();
    ArrayList<Object> objectTrack = new ArrayList<>();
    ArrayList<Object> objectGhost = new ArrayList<>();

    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());
    float distance = 1f;
    float angle = (float)Math.toRadians(1f);
    float rotation = (float)Math.toRadians(1f);
    float moveChar = 0.5f;
    float ghostSpd = 0.5f;
    ArrayList<Vector3f> moveGhosts = new ArrayList<Vector3f>();
    float moveCam = 0.7f;
    float rotateVal = 0.f;
    float rotateDiv = 0.f;
    float rotating = (float)Math.toRadians(1f);
    List<Float> temp;
    int modeToggle = 0;
    boolean delay = false;
    int delayCounter = 0;

    public void run() throws IOException {

        init();
        loop();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() throws IOException {
        window.init();
        GL.createCapabilities();
        //camera.setPosition(-6.485f, 5.756f, -3.464f);
        //camera.setRotation((float) Math.toRadians(10), (float) Math.toRadians(90));

        camera.setPosition(-6.485f, 20f, -3.464f);
        camera.setRotation((float) Math.toRadians(60), (float) Math.toRadians(90));

        // Pacman
        objectObj.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
                "resources/model/pacman/karakterpacman/pacmann.obj",
                "pacman",
                new Vector3f(1.f, 1.f, 1.f), 1
        ));
        objectObj.get(0).scaleObject(0.01f, 0.01f, 0.01f);
        objectObj.get(0).translateObject(2.0f, 2.0f, 7.0f);

        //Ground
        objectGround.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1.0f, 0.5f, 1.0f, 1.0f),
                "resources/model/tanah.obj",
                "ground",
                new Vector3f(100.f, 0.1f, 100.f), 1
        ));
        objectGround.get(0).translateObject(60.0f, .0f, 5.0f);

        // Track
        float gapZ1 = 7.5f, gapZ2 = 8.5f, gapX1 = 8.5f, gapX2 = 7.5f;
        Vector3f[] obPos = {
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * -1),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * -1),
                new Vector3f(3.5f + gapX2 * -1, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * 0),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 0),

                new Vector3f(3.5f + gapX2 * 0, 1.8f, -3.5f + gapZ2 * 2),
                new Vector3f(3.5f + gapX2 * 1, 1.8f, -3.5f + gapZ2 * 2),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 2),
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * 2),
                new Vector3f(3.5f + gapX2 * 2, 1.8f, -3.5f + gapZ2 * 0),

                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(3.5f + gapX2 * 4, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(3.5f + gapX2 * 6, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * 7),

                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(0.5f + gapX1 * 2, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(3.5f + gapX2 * 6, 1.8f, -3.5f + gapZ2 * 5),

                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 5),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(0.5f + gapX1 * 7, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 7),

                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * 7),
                new Vector3f(3.5f + gapX2 * 6, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(3.5f + gapX2 * 4, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 10),

                new Vector3f(3.5f + gapX2 * 2, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * 2),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 2),
                new Vector3f(3.5f + gapX2 * 1, 1.8f, -3.5f + gapZ2 * 8),
                new Vector3f(3.5f + gapX2 * 0, 1.8f, -3.5f + gapZ2 * 8),

                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 0),
                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * 0),
                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * -1),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * -1),
                new Vector3f(3.5f + gapX2 * -2, 1.8f, -3.5f + gapZ2 * 8),

                new Vector3f(3.5f + gapX2 * -3, 1.8f, -3.5f + gapZ2 * 8),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * -3),
                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * -3),
                new Vector3f(3.5f + gapX2 * -4, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(3.5f + gapX2 * -5, 1.8f, -3.5f + gapZ2 * 10),

                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(3.5f + gapX2 * -7, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(3.5f + gapX2 * -8, 1.8f, -3.5f + gapZ2 * 10),
                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * -8),

                new Vector3f(0.5f + gapX1 * 7, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * -8),

                new Vector3f(0.5f + gapX1 * 2, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * -8),
                new Vector3f(3.5f + gapX2 * -8, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(3.5f + gapX2 * -7, 1.8f, -3.5f + gapZ2 * 0),

                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(3.5f + gapX2 * -5, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(3.5f + gapX2 * -4, 1.8f, -3.5f + gapZ2 * 0),
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * -3),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * -3),

                new Vector3f(3.5f + gapX2 * -3, 1.8f, -3.5f + gapZ2 * 2),
                new Vector3f(3.5f + gapX2 * -2, 1.8f, -3.5f + gapZ2 * 2),

                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 3),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 4),
                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 2),
                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 1),

                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 5),
                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * 6),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 2),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 1),

                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * 5),
                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * 6),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 4),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 3),

                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * 5),
                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * 6),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 6),

                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 5),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 6),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 9),
                new Vector3f(3.5f + gapX2 * 5, 1.8f, -3.5f + gapZ2 * 8),

                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 3),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * 4),
                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 9),
                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 8),

                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(3.5f + gapX2 * 2, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(3.5f + gapX2 * 1, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(3.5f + gapX2 * 0, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * 2),

                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 3),
                new Vector3f(3.5f + gapX2 * 2, 1.8f, -3.5f + gapZ2 * 3),
                new Vector3f(3.5f + gapX2 * 1, 1.8f, -3.5f + gapZ2 * 3),
                new Vector3f(3.5f + gapX2 * 0, 1.8f, -3.5f + gapZ2 * 3),
                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * 2),

                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 4),
                new Vector3f(3.5f + gapX2 * 3, 1.8f, -3.5f + gapZ2 * 6),
                new Vector3f(3.5f + gapX2 * 2, 1.8f, -3.5f + gapZ2 * 5),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * 4),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * 4),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * 3),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * 3),

                new Vector3f(3.5f + gapX2 * -3, 1.8f, -3.5f + gapZ2 * 4),
                new Vector3f(3.5f + gapX2 * -3, 1.8f, -3.5f + gapZ2 * 6),
                new Vector3f(3.5f + gapX2 * -4, 1.8f, -3.5f + gapZ2 * 5),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * -2),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * -2),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * -3),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * -3),

                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 4),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 6),
                new Vector3f(3.5f + gapX2 * -7, 1.8f, -3.5f + gapZ2 * 5),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * -5),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * -5),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * -6),
                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * -6),

                new Vector3f(0.5f + gapX1 * 4, 1.8f, 1.0f - gapZ1 * -1),
                new Vector3f(0.5f + gapX1 * 5, 1.8f, 1.0f - gapZ1 * -1),
                new Vector3f(3.5f + gapX2 * 0, 1.8f, -3.5f + gapZ2 * 4),
                new Vector3f(3.5f + gapX2 * -1, 1.8f, -3.5f + gapZ2 * 4),
                new Vector3f(3.5f + gapX2 * 0, 1.8f, -3.5f + gapZ2 * 6),
                new Vector3f(3.5f + gapX2 * -1, 1.8f, -3.5f + gapZ2 * 6),

                new Vector3f(3.5f + gapX2 * -2, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(3.5f + gapX2 * -3, 1.8f, -3.5f + gapZ2 * 7),

                new Vector3f(3.5f + gapX2 * -2, 1.8f, -3.5f + gapZ2 * 3),
                new Vector3f(3.5f + gapX2 * -3, 1.8f, -3.5f + gapZ2 * 3),

                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * -4),
                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * -4),

                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * -4),
                new Vector3f(3.5f + gapX2 * -5, 1.8f, -3.5f + gapZ2 * 2),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 2),

                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * -4),
                new Vector3f(3.5f + gapX2 * -5, 1.8f, -3.5f + gapZ2 * 8),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 8),

                new Vector3f(0.5f + gapX1 * 1, 1.8f, 1.0f - gapZ1 * -7),
                new Vector3f(0.5f + gapX1 * 2, 1.8f, 1.0f - gapZ1 * -7),
                new Vector3f(0.5f + gapX1 * 3, 1.8f, 1.0f - gapZ1 * -7),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 3),
                new Vector3f(3.5f + gapX2 * -7, 1.8f, -3.5f + gapZ2 * 3),

                new Vector3f(0.5f + gapX1 * 6, 1.8f, 1.0f - gapZ1 * -7),
                new Vector3f(0.5f + gapX1 * 7, 1.8f, 1.0f - gapZ1 * -7),
                new Vector3f(0.5f + gapX1 * 8, 1.8f, 1.0f - gapZ1 * -7),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 7),
                new Vector3f(3.5f + gapX2 * -7, 1.8f, -3.5f + gapZ2 * 7),

                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * -5),
                new Vector3f(0.5f + gapX1 * 0, 1.8f, 1.0f - gapZ1 * -6),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 1),

                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * -5),
                new Vector3f(0.5f + gapX1 * 9, 1.8f, 1.0f - gapZ1 * -6),
                new Vector3f(3.5f + gapX2 * -6, 1.8f, -3.5f + gapZ2 * 9),
        };

        int[] obType = {
          1, 1, 2, 1, 1,
          2, 2, 1, 1, 2,
          2, 2, 2, 2, 1,
          1, 1, 1, 1, 2,
          2, 1, 1, 1, 1,
          1, 2, 2, 2, 2,
          2, 1, 1, 2, 2,
          1, 1, 1, 1, 2,
          2, 1, 1, 2, 2,
          2, 2, 2, 1, 1,
          1, 1, 1, 1, 1,
          1, 1, 1, 2, 2,
          2, 2, 2, 1, 1,
          2, 2,

          1, 1, 2, 2,
          1, 1, 2, 2,
          1, 1, 2, 2,
          1, 1, 2, 2,
          1, 1, 2, 2,
          1, 1, 2, 2,

          2, 2, 2, 2, 1,
          2, 2, 2, 2, 1,

          2, 2, 2, 1, 1, 1, 1,
          2, 2, 2, 1, 1, 1, 1,
          2, 2, 2, 1, 1, 1, 1,

          1, 1, 2, 2, 2, 2,

          2, 2,
          2, 2,
          1, 1,

          1, 2, 2,
          1, 2, 2,

          1, 1, 1, 2, 2,
          1, 1, 1, 2, 2,

          1, 1, 2,
          1, 1, 2,
        };

        for (int i = 0; i < obPos.length; i++) {
            objectTrack.add(new Model(
                    Arrays.asList(
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                    ),
                    new ArrayList<>(),
                    new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),
                    "resources/model/wall.obj",
                    "track" + i,
                    new Vector3f(10.f, 3.f, 1.f),
                    obType[i]
            ));
            objectTrack.get(i).scaleObject(1.5f, 1.5f, 1.5f);
            objectTrack.get(i).translateObject(obPos[i].x, obPos[i].y, obPos[i].z);
            if (obType[i] == 2)
                objectTrack.get(i).rotateObject((float) Math.toRadians(90), 0.f, 1.f, 0.f);
        }

        // Ghost
        Vector3f[] ghostPos = {
                new Vector3f(40.f, 2.f, 25.f),
                new Vector3f(25.f, 2.f, 32.f),
                new Vector3f(5.f, 2.f, 25.f),

                new Vector3f(40.f, 2.f, 32.f),
                new Vector3f(30.f, 2.f, 0.f),
                new Vector3f(5.f, 2.f, 32.f),

                new Vector3f(40.f, 2.f, -11.f),
                new Vector3f(25.f, 2.f, -11.f),
                new Vector3f(10.f, 2.f, -32.f),

                new Vector3f(40.f, 2.f, -18.f),
                new Vector3f(25.f, 2.f, -18.f),
                new Vector3f(10.f, 2.f, -18.f),
        };


        for (int i = 0; i < ghostPos.length; i++) {
            int random = (int) (10 * Math.random() % 4);
            moveGhosts.add(new Vector3f(random == 0 ? ghostSpd : random == 1 ? -ghostSpd : 0.f, 0.f, random == 2 ? ghostSpd : random == 3 ? -ghostSpd : 0.f));
            objectGhost.add(new Model(
                    Arrays.asList(
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                    ),
                    new ArrayList<>(),
                    new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),
                    "resources/model/pacman/karakterpacman/pacman_ghost.obj",
                    "ghost",
                    new Vector3f(.5f, .5f, .5f),
                    1
            ));
            objectGhost.get(i).scaleObject(0.05f, 0.05f, 0.05f);
            objectGhost.get(i).translateObject(ghostPos[i].x, ghostPos[i].y, ghostPos[i].z);
        }

    }
    public boolean charColliding(Object character, float x, float y, float z) {
        Object chara = character;
        for(int i = 0; i < objectTrack.size(); i++){
            if(checkCollision(chara, objectTrack.get(i), x, y, z)) return true;
        }
        return false;
    }
    public void input() {
        boolean isColliding = false;
        Object chara = objectObj.get(0);

        temp = chara.getCenterPoint();
        angle = angle % (float) Math.toRadians(360);

        if (window.isKeyPressed(GLFW_KEY_F) && !delay){
            modeToggle++;
            modeToggle = modeToggle % 2;
            System.out.println("Model Toggle: " + modeToggle);
            delay = true;
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            if (modeToggle == 1) {
                camera.moveLeft(moveCam);
            } else if (modeToggle == 0) {
                isColliding = charColliding(chara, 0, 0, -moveChar);
                if (!isColliding) {
                    chara.translateObject(0f, 0f, -moveChar);
                }
            }
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            if (modeToggle == 1) {
                camera.moveBackwards(moveCam);
            } else if (modeToggle == 0) {
                isColliding = charColliding(chara, -moveChar, 0, 0);
                if (!isColliding) {
                    chara.translateObject(-moveChar, 0f, 0f);
                }
            }
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            if (modeToggle == 1) {
                camera.moveRight(moveCam);
            } else if (modeToggle == 0) {
                isColliding = charColliding(chara, 0, 0, moveChar);
                if (!isColliding) {
                    objectObj.get(0).translateObject(0f, 0f, moveChar);
                }
            }
        }

        if (window.isKeyPressed(GLFW_KEY_W)) {
            if (modeToggle == 1) {
                camera.moveForward(moveCam);
            } else if (modeToggle == 0) {
                isColliding = charColliding(chara, moveChar, 0, 0);
                if (!isColliding) {
                    objectObj.get(0).translateObject(moveChar, 0f, 0f);
                }
            }
        }

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            if (modeToggle == 1) {
                camera.addRotation(-rotating, 0f);
            } else {
                camera.moveForward(distance);
                camera.addRotation(-rotating, 0f);
                camera.moveBackwards(distance);
            }
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            if (modeToggle == 1) {
                camera.addRotation(rotating, 0f);
            } else {
                camera.moveForward(distance);
                camera.addRotation(rotating, 0f);
                camera.moveBackwards(distance);
            }
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            if (modeToggle == 1) {
                camera.addRotation(0f, -rotating);
            } else {
                camera.moveForward(distance);
                camera.addRotation(0f, -rotating);
                camera.moveBackwards(distance);
            }
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            if (modeToggle == 1) {
                camera.addRotation(0f, rotating);
            } else {
                camera.moveForward(distance);
                camera.addRotation(0f, rotating);
                camera.moveBackwards(distance);
            }
        }

        if (window.getMouseInput().isRightButtonPressed()) {
            Vector2f displVec = window.getMouseInput().getDisplVec();
            if (modeToggle == 1) {
                camera.addRotation((float) Math.toRadians(displVec.x * 0.1f), (float) Math.toRadians(displVec.y * 0.1f));
            } else {
                camera.moveForward(distance);
                camera.addRotation((float) Math.toRadians(displVec.x * 0.1f), (float) Math.toRadians(displVec.y * 0.1f));
                camera.moveBackwards(distance);
            }
        }

        if (window.getMouseInput().getScroll().y != 0) {
            projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.1f));
            window.getMouseInput().setScroll(new Vector2f());
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            if (modeToggle == 1) {
                camera.moveUp(moveCam);
            }
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            if (modeToggle == 1) {
                camera.moveDown(moveCam);
            }
        }
    }

//    public void rotate(String type) {
//        rotateVal = (float)Math.toRadians(0.f);
//        rotateDiv = (float)Math.toRadians(0.5f);;
//        if (type == "Right") {
//            rotateVal = (float) Math.toRadians(-90.f);
//            rotateDiv *= -1;
//        } else if (type == "Left") {
//            rotateVal = (float) Math.toRadians(90.f);
//        }
//        Object chara = objectObj.get(0);
//        chara.rotateObject(rotateDiv, 0.f, 1f, 0.f);
//        angle += rotateDiv;
//    }
    public boolean checkCollision(Object box1, Object box2, float x, float y, float z) {
        String name1 = box1.getName();
        String name2 = box2.getName();

        // Get the position of box1
        List<Float> obj_pos = box1.getPosition();
        float x1 = obj_pos.get(0) + x + (box1.getName() == "ghost" ? 1.1f : 0.f);
        float y1 = obj_pos.get(1) + y;
        float z1 = obj_pos.get(2) + z + (box1.getName() == "ghost" ? 3.1f : 0.f);

        // Get the position of box2
        List<Float> ground_pos = box2.getPosition();
        float x2 = ground_pos.get(0);
        float y2 = ground_pos.get(1);
        float z2 = ground_pos.get(2);

        // Calculate the boundaries of box1
        float minX1 = x1 - box1.getSize().x / 2.0f;
        float maxX1 = x1 + box1.getSize().x / 2.0f;

        float minY1 = y1 - box1.getSize().y / 2.0f;
        float maxY1 = y1 + box1.getSize().y / 2.0f;

        float minZ1 = z1 - box1.getSize().z / 2.0f;
        float maxZ1 = z1 + box1.getSize().z / 2.0f;

        // Calculate the boundaries of box2
        float minX2 = 0.f, maxX2 = 0.f, minZ2 = 0.f, maxZ2 = 0.f;

        if (box2.getType() == 1) {
            minX2 = x2 - box2.getWidth() / 2.0f - 1.0f;
            maxX2 = x2 + box2.getWidth() / 2.0f + 3.0f;

            minZ2 = z2 - box2.getDepth() / 2.0f + 2.0f;
            maxZ2 = z2 + box2.getDepth() / 2.0f + 3.0f;
        } else if (box2.getType() == 2) {
            minX2 = x2 - box2.getDepth() / 2.0f;
            maxX2 = x2 + box2.getDepth() / 2.0f + 1.0f;

            minZ2 = z2 - box2.getWidth() / 2.0f + 1.6f;
            maxZ2 = z2 + box2.getWidth() / 2.0f + 5.4f;
        }

        float minY2 = 2*y2 - box2.getHeight();
        float maxY2 = 2*y2 + box2.getHeight();

//         System.out.println("Compare: " + name1 + " with " + name2);
//         System.out.println(name1 + ": x=" + minX1 + " to " + maxX1 + "; y=" + minY1 + " to " + maxY1 + "; z=" + minZ1 + " to " + maxZ1);
//         System.out.println(name2 + ": x=" + minX2 + " to " + maxX2 + "; y=" + minY2 + " to " + maxY2 + "; z=" + minZ2 + " to " + maxZ2 + "\n");

        // Check for collision along axis
        if (!((minX1 < minX2 && maxX1 < minX2) || (minX1 > maxX2 && maxX1 > maxX2)) &&
                !((minY1 < minY2 && maxY1 < minY2) || (minY1 > maxY2 && maxY1 > maxY2)) &&
                !((minZ1 < minZ2 && maxZ1 < minZ2) || (minZ1 > maxZ2 && maxZ1 > maxZ2))
        ) {
             // System.out.println("Collides\n\n");
            return true;
        }

         // System.out.println("No Colliding\n\n");

        return false;
    }
    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL.createCapabilities();

            input();

            // System.out.println("Angle: " + this.angle);
            // System.out.println("Coord: " + camera.getPosition().x + ", " + camera.getPosition().y + ", " + camera.getPosition().z);
            // List<Float> cp = this.objectObj.get(0).getCenterPoint();
            // System.out.println("Center point: " + cp.get(0) + ", " + cp.get(1) + ", " + cp.get(2));

            for (int i = 0; i < objectGhost.size(); i++) {
                Vector3f dirs = moveGhosts.get(i);
                if(charColliding(objectGhost.get(i), dirs.x, dirs.y, dirs.z)) {
                    int random = (int) (10 * Math.random() % 4);
                    moveGhosts.get(i).set(new Vector3f(random == 0 ? ghostSpd : random == 1 ? -ghostSpd : 0.f, 0.f, random == 2 ? ghostSpd : random == 3 ? -ghostSpd : 0.f));
                } else
                    objectGhost.get(i).translateObject(dirs.x, dirs.y, dirs.z);
            }

            if (delay){
                delayCounter++;
            }

            if (delayCounter > 30){
                delayCounter = 0;
                delay = false;
            }

            // code here
            for (Object object: objectObj) {
                object.draw(camera, projection);
            }

            for (Object object: objectGround) {
                object.draw(camera, projection);
            }

            for (Object object: objectTrack){
                object.draw(camera, projection);
            }

            for (Object object: objectGhost){
                object.draw(camera, projection);
            }

            // Restore state
            glDisableVertexAttribArray(0);
            // Poll for window events.
            // The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }
}
