import Engine.*;
import Engine.Object;
import org.joml.*;
import org.lwjgl.opengl.GL;

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
// haloo
public class Main {
    private Window window = new Window(1080, 1080, "Hello World");
    ArrayList<Object> objectObj = new ArrayList<>();
    ArrayList<Object> objectGround = new ArrayList<>();
    ArrayList<Object> objectTrack = new ArrayList<>();
    ArrayList<Object> objectOuterWall = new ArrayList<>();
    ArrayList<Object> objectFinishLine = new ArrayList<>();
    ArrayList<Object> objectLighthouse = new ArrayList<>();
    ArrayList<Object> objectPagar= new ArrayList<>();
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());
    float distance = 1f;
    float angle = 0f;
    float rotation = (float)Math.toRadians(1f);
    float move = 0.01f;
    List<Float> temp;
    int carPos = 0;
    int modeToggle = 0;
    int carPos2 = 0;
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
        camera.setPosition(-1.7f, 1f, 2.5f + distance);

        // mobil (ObjectObj(0))
        objectObj.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1.0f,0.0f,0.0f,1.0f),
                "resources/model/mobil/mobil.obj"
        ));
        objectObj.get(0).scaleObject(0.1f,0.1f,0.1f);
        objectObj.get(0).translateObject(-0.2f, 0f, 0f);

        objectObj.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.0f,0.0f,1.0f,1.0f),
                "resources/model/mobil/mobil.obj"
        ));
        objectObj.get(1).scaleObject(0.1f,0.1f,0.1f);
        objectObj.get(1).translateObject(0.2f, 0f, 0f);

        // ground (objectGround(0))
        objectGround.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.01f,0.59f,0.17f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectGround.get(0).scaleObject(20f ,1f, 20f);
        objectGround.get(0).translateObject(0f, -0.565f, 0f);

        //Track
        objectTrack.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));

        //Track
        objectTrack.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).translateObject(0f,-0.564f,0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(0).translateObject(0.0f,-0.564f,-1.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(1).translateObject(.0f,-0.564f,-2.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(2).translateObject(.0f,-0.564f,-3.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(3).translateObject(.0f,-0.564f,-4.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(4).translateObject(.0f,-0.564f,-5.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(5).translateObject(.0f,-0.564f,-6.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(6).translateObject(.0f,-0.564f,-7.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(7).translateObject(3.19f,-0.555f,-5.6f);
        objectTrack.get(0).getChildObject().get(7).rotateObject((float) Math.toRadians(1.0f),0f,30.0f,0f);


        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(8).translateObject(1.0f,-0.564f,-7.53f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(9).translateObject(2.0f,-0.564f,-7.53f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(10).translateObject(-0.83f,-0.555f,-6.83f);
        objectTrack.get(0).getChildObject().get(10).rotateObject((float) Math.toRadians(1.0f),0f,-30.0f,0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(11).translateObject(3.0f,-0.564f,-7.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(12).translateObject(3.0f,-0.564f,-6.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(13).translateObject(3.0f,-0.564f,-5.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(14).translateObject(3.0f,-0.564f,-4.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(15).translateObject(3.0f,-0.564f,-3.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(16).translateObject(3.0f,-0.564f,-2.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(17).translateObject(3.0f,-0.564f,-1.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(18).translateObject(3.0f,-0.564f,-.0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(19).translateObject(2.04f,-0.555f,1.34f);
        objectTrack.get(0).getChildObject().get(19).rotateObject((float) Math.toRadians(1.0f),0f,30.0f,0f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(20).translateObject(2.0f,-0.564f,0.537f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(21).translateObject(1.0f,-0.564f,0.537f);

        objectTrack.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.5f,0.5f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectTrack.get(0).getChildObject().get(22).translateObject(0.318f,-0.555f,0.11f);
        objectTrack.get(0).getChildObject().get(22).rotateObject((float) Math.toRadians(1.0f),0f,-30.0f,0f);


        //OUTER WALL
        //kiri
        objectOuterWall.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.8f,0.f,0.f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"

        ));

        objectOuterWall.get(0).translateObject(-0.02f,-1.0f,-0.435f);
        objectOuterWall.get(0).scaleObject(1.0f,1.0f,8.1f);
        objectOuterWall.get(0).rotateObject((float)Math.toRadians(90.0f),.0f,.0f,-1.0f);

        //kanan
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.8f,0.f,0.f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));

        objectOuterWall.get(0).getChildObject().get(0).translateObject(-0.02f,3.0f,-0.435f);
        objectOuterWall.get(0).getChildObject().get(0).scaleObject(1.0f,1.0f,8.1f);
        objectOuterWall.get(0).getChildObject().get(0).rotateObject((float) Math.toRadians(90.0f),0f,0.0f,-1.0f);


        //sudut kiri atas
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f,0.9f,0.9f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));

        objectOuterWall.get(0).getChildObject().get(1).translateObject(-0.02f,-7.4f,-3.18f);
        objectOuterWall.get(0).getChildObject().get(1).scaleObject(1.0f,1.0f,1.1f);
        objectOuterWall.get(0).getChildObject().get(1).rotateObject((float) Math.toRadians(90.0f),0f,0.0f,-1.0f);
        objectOuterWall.get(0).getChildObject().get(1).rotateObject((float) Math.toRadians(63.0f),0f,-1.0f,.0f);


        //atas
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.8f,0.f,0.f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));

        objectOuterWall.get(0).getChildObject().get(2).translateObject(0.7f,-8.48f,-0.02f);
        objectOuterWall.get(0).getChildObject().get(2).scaleObject(2.2f,1.0f,1.0f);
        objectOuterWall.get(0).getChildObject().get(2).rotateObject((float) Math.toRadians(90.0f),1.0f,0.0f,.0f);

        //bawah
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),new Vector4f(0.8f,0.f,0.f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));

        objectOuterWall.get(0).getChildObject().get(3).translateObject(0.7f,0.5f,-0.02f);
        objectOuterWall.get(0).getChildObject().get(3).scaleObject(2.2f,1.0f,1.0f);
        objectOuterWall.get(0).getChildObject().get(3).rotateObject((float) Math.toRadians(90.0f),1.0f,0.0f,.0f);



        //sudut kanan atas
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f,0.9f,0.9f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));
        objectOuterWall.get(0).getChildObject().get(4).translateObject(-0.02f,7.77f,-0.7f);
        objectOuterWall.get(0).getChildObject().get(4).scaleObject(1.0f,1.0f,1.1f);
        objectOuterWall.get(0).getChildObject().get(4).rotateObject((float) Math.toRadians(90.0f),0f,0.0f,-1.0f);
        objectOuterWall.get(0).getChildObject().get(4).rotateObject((float) Math.toRadians(-63.0f),0f,-1.0f,.0f);


        //sudut kanan bawah
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f,0.9f,0.9f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));

        objectOuterWall.get(0).getChildObject().get(5).translateObject(-0.02f,1.53f,-2.13f);
        objectOuterWall.get(0).getChildObject().get(5).scaleObject(1.0f,1.0f,1.1f);
        objectOuterWall.get(0).getChildObject().get(5).rotateObject((float) Math.toRadians(90.0f),0f,0.0f,-1.0f);
        objectOuterWall.get(0).getChildObject().get(5).rotateObject((float) Math.toRadians(63.0f),0f,-1.0f,.0f);


        //sudut kiri bawah
        objectOuterWall.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f,0.9f,0.9f,1.0f),
                "resources/model/track/Terrain_Grass_Flat_1x1.obj"
        ));



        objectOuterWall.get(0).getChildObject().get(6).translateObject(-0.02f,-1.17f,0.302f);
        objectOuterWall.get(0).getChildObject().get(6).scaleObject(1.0f,1.0f,1.1f);
        objectOuterWall.get(0).getChildObject().get(6).rotateObject((float) Math.toRadians(90.0f),0f,0.0f,-1.0f);
        objectOuterWall.get(0).getChildObject().get(6).rotateObject((float) Math.toRadians(-63.0f),0f,-1.0f,.0f);

        objectOuterWall.get(0).translateObject(-1.5f, 0f, 3.5f);
        objectTrack.get(0).translateObject(-1.5f, 0f, 3.5f);

        objectTrack.get(0).translateObject(0f, 1f, 0f);
        objectObj.get(0).translateObject(-1.5f, 1f, 2.5f);
        objectObj.get(1).translateObject(-1.5f, 1f, 2.5f);
        objectGround.get(0).translateObject(0f, 1f, 0f);
        objectOuterWall.get(0).translateObject(0f, 1f, 0f);

        //lighthouse
        //atap dan bagian merah
        objectLighthouse.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.f,0.f,1.0f),
                "resources/model/track/lighthouse_part_1.obj"
        ));
        objectLighthouse.get(0).translateObject(-1.15f,8.88f,3.55f);
        objectLighthouse.get(0).scaleObject(0.15f,0.15f,0.15f);

        //kaki merah
        objectLighthouse.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f,0.f,0.f,1.0f),
                "resources/model/track/lighthouse_part_2.obj"
        ));
        objectLighthouse.get(0).getChildObject().get(0).translateObject(1.0f,7.0f,1.0f);
        objectLighthouse.get(0).getChildObject().get(0).scaleObject(0.15f,0.15f,0.15f);


        //bagian putih
        objectLighthouse.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.7f,0.7f,0.7f,1.0f),
                "resources/model/track/lighthouse_part_3.obj"
        ));
        objectLighthouse.get(0).getChildObject().get(1).translateObject(-0.1f,6.9f,3.6f);
        objectLighthouse.get(0).getChildObject().get(1).scaleObject(0.15f,0.15f,0.15f);


        //lampu
        objectLighthouse.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1.0f,1.0f,0.0f,1.0f),
                "resources/model/track/lighthouse_part_lights.obj"
        ));
        objectLighthouse.get(0).getChildObject().get(2).translateObject(0.1f,2.5f,4.0f);
        objectLighthouse.get(0).getChildObject().get(2).scaleObject(0.15f,0.2f,0.15f);


        //pagar1
        objectPagar.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.2f,0.2f,0.2f,1.0f),
                "resources/model/track/Prop_Decorative_Fence_Curve_5x6.obj"
        ));
        objectPagar.get(0).translateObject(-2.5f,9.3f,9.5f);
        objectPagar.get(0).scaleObject(0.1f,0.1f,0.1f);


        //pagar2
        objectPagar.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.2f,0.2f,0.2f,1.0f),
                "resources/model/track/Prop_Decorative_Fence_Curve_5x6.obj"
        ));
        objectPagar.get(0).getChildObject().get(0).translateObject(-8.0f,9.3f,1.0f);
        objectPagar.get(0).getChildObject().get(0).scaleObject(0.1f,0.1f,0.1f);
        objectPagar.get(0).getChildObject().get(0).rotateObject((float)Math.toRadians(120.0f),0.f,1.0f,0.f);


        //pagar3
        objectPagar.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.2f,0.2f,0.2f,1.0f),
                "resources/model/track/Prop_Decorative_Fence_Curve_5x6.obj"
        ));
        objectPagar.get(0).getChildObject().get(1).translateObject(2.0f,9.3f,0.f);
        objectPagar.get(0).getChildObject().get(1).scaleObject(0.1f,0.1f,0.1f);
        objectPagar.get(0).getChildObject().get(1).rotateObject((float)Math.toRadians(-128.0f),0.f,1.0f,0.f);


       //finish line
        objectFinishLine.add(new Model(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.f,0.f,0.f,1.0f),
                "resources/model/track/finish_linee.obj"
        ));
        objectFinishLine.get(0).translateObject(-3.35f,1.449f,4.0f);
        objectFinishLine.get(0).scaleObject(0.5f,0.5f,0.5f);

    }

    public void input() {
        boolean isColliding = false;
        temp = objectObj.get(0).getCenterPoint();
        angle = angle % (float) Math.toRadians(360);

        if (window.isKeyPressed(GLFW_KEY_F) && !delay){
            modeToggle++;
            modeToggle = modeToggle % 3;
            System.out.println("Model Toggle: " + modeToggle);
            delay = true;
        }

        if (window.isKeyPressed(GLFW_KEY_W)) {
            if (modeToggle == 2) {
                camera.moveForward(move);
            } else if (modeToggle == 0) {
                for(Object object: objectOuterWall){
                    isColliding = checkCollision(objectObj.get(0), object);
                    for(Object objectChild: object.getChildObject()){
                        isColliding = checkCollision(objectObj.get(0), objectChild);
                    }
                }
                if (!isColliding) {
                    objectObj.get(0).translateObject(0f, 0f, -move);
                    camera.setPosition(temp.get(0), temp.get(1), temp.get(2));
                    camera.moveBackwards(distance);
                    if (angle > (float) Math.toRadians(0) && angle < (float) Math.toRadians(180)) {
                        objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                        objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                        objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                        angle = angle - rotation;
                    } else if (angle > (float) Math.toRadians(180) && angle < (float) Math.toRadians(360)) {
                        objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                        objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                        objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                        angle = angle + rotation;
                    } else if (angle > (float) Math.toRadians(-180) && angle < (float) Math.toRadians(0)) {
                        objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                        objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                        objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                        angle = angle + rotation;
                    } else if (angle > (float) Math.toRadians(-360) && angle < (float) Math.toRadians(-180)) {
                        objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                        objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                        objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                        angle = angle - rotation;
                    }
                }
            }
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            if (modeToggle == 2) {
                camera.moveLeft(move);
            } else if (modeToggle == 0) {
                objectObj.get(0).translateObject(-move, 0f, 0f);
                camera.setPosition(temp.get(0), temp.get(1), temp.get(2));
                camera.moveBackwards(distance);
                if (angle > (float) Math.toRadians(90) && angle < (float) Math.toRadians(270)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                } else if (angle > (float) Math.toRadians(270) && angle < (float) Math.toRadians(450)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                } else if (angle > (float) Math.toRadians(-90) && angle < (float) Math.toRadians(90)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                } else if (angle > (float) Math.toRadians(-270) && angle < (float) Math.toRadians(-90)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                } else if (angle > (float) Math.toRadians(-360) && angle < (float) Math.toRadians(-270)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                }
            }
        }

        if (window.isKeyPressed(GLFW_KEY_S)) {
            if (modeToggle == 2) {
                camera.moveBackwards(move);
            } else if (modeToggle == 0) {
                objectObj.get(0).translateObject(0f, 0f, move);
                camera.setPosition(temp.get(0), temp.get(1), temp.get(2));
                camera.moveBackwards(distance);
                if (angle > (float) Math.toRadians(180) && angle < (float) Math.toRadians(360)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                } else if (angle > (float) Math.toRadians(0) && angle < (float) Math.toRadians(180)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                } else if (angle > (float) Math.toRadians(-360) && angle < (float) Math.toRadians(-180)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                } else if (angle > (float) Math.toRadians(-180) && angle < (float) Math.toRadians(0)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                }
            }
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            if (modeToggle == 2) {
                camera.moveRight(move);
            } else if (modeToggle == 0) {
                objectObj.get(0).translateObject(move, 0f, 0f);
                camera.setPosition(temp.get(0), temp.get(1), temp.get(2));
                camera.moveBackwards(distance);
                if (angle > (float) Math.toRadians(-90) && angle < (float) Math.toRadians(90)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                } else if (angle > (float) Math.toRadians(90) && angle < (float) Math.toRadians(270)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                } else if (angle > (float) Math.toRadians(270) && angle < (float) Math.toRadians(360)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                } else if (angle > (float) Math.toRadians(-270) && angle < (float) Math.toRadians(-90)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle + rotation;
                } else if (angle > (float) Math.toRadians(-450) && angle < (float) Math.toRadians(-270)) {
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-rotation, 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - rotation;
                }
            }
        }

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            if (modeToggle == 2) {
                camera.addRotation(-0.01f, 0f);
            } else {
                camera.moveForward(distance);
                camera.addRotation(-0.01f, 0f);
                camera.moveBackwards(distance);
            }
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            if (modeToggle == 2) {
                camera.addRotation(0.01f, 0f);
            } else {
                camera.moveForward(distance);
                camera.addRotation(0.01f, 0f);
                camera.moveBackwards(distance);
            }
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            if (modeToggle == 2) {
                camera.addRotation(0f, -0.01f);
            } else {
                camera.moveForward(distance);
                camera.addRotation(0f, -0.01f);
                camera.moveBackwards(distance);
            }
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            if (modeToggle == 2) {
                camera.addRotation(0f, 0.01f);
            } else {
                camera.moveForward(distance);
                camera.addRotation(0f, 0.01f);
                camera.moveBackwards(distance);
            }
        }

        if (window.getMouseInput().isRightButtonPressed()) {
            Vector2f displVec = window.getMouseInput().getDisplVec();
            if (modeToggle == 2) {
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
            if (modeToggle == 2) {
                camera.moveUp(move);
            }
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            if (modeToggle == 2) {
                camera.moveDown(move);
            }
        }
    }

    public boolean checkCollision(Object box1, Object box2) {
        // Get the position of box1
        List<Float> obj_pos = box1.getPosition();
        float x1 = obj_pos.get(0);
        float y1 = obj_pos.get(1);
        float z1 = obj_pos.get(2);

        // Get the position of box2
        List<Float> ground_pos = box2.getPosition();
        float x2 = ground_pos.get(0);
        float y2 = ground_pos.get(1);
        float z2 = ground_pos.get(2);

        // Calculate the boundaries of box1
        float minX1 = x1 - box1.getWidth() / 2.0f;
        float maxX1 = x1 + box1.getWidth() / 2.0f;
        float minY1 = y1 - box1.getHeight() / 2.0f;
        float maxY1 = y1 + box1.getHeight() / 2.0f;
        float minZ1 = z1 - box1.getDepth() / 2.0f;
        float maxZ1 = z1 + box1.getDepth() / 2.0f;

        // Calculate the boundaries of box2
        float minX2 = x2 - box2.getWidth() / 2.0f;
        float maxX2 = x2 + box2.getWidth() / 2.0f;
        float minY2 = y2 - box2.getHeight() / 2.0f;
        float maxY2 = y2 + box2.getHeight() / 2.0f;
        float minZ2 = z2 - box2.getDepth() / 2.0f;
        float maxZ2 = z2 + box2.getDepth() / 2.0f;

        // Check for collision along each axis
        if (maxX1 <= minX2 || minX1 >= maxX2) {
            return false; // No collision along X-axis
        }
        if (maxY1 <= minY2 || minY1 >= maxY2) {
            return false; // No collision along Y-axis
        }
        if (maxZ1 <= minZ2 || minZ1 >= maxZ2) {
            return false; // No collision along Z-axis
        }

        return true; // Colliding on all axes
    }

    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL.createCapabilities();

            input();

            if (delay){
                delayCounter++;
            }

            if (delayCounter > 30){
                delayCounter = 0;
                delay = false;
            }

            if (carPos < 900) {
                objectObj.get(1).translateObject(0f, 0f, -0.007f);
                carPos++;
            }

            if (900 <= carPos && carPos < 990) {
                List<Float> temp = objectObj.get(1).getCenterPoint();
                objectObj.get(1).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                objectObj.get(1).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                objectObj.get(1).translateObject(temp.get(0), temp.get(1), temp.get(2));
            }

            if (900 <= carPos && carPos < 1250) {
                objectObj.get(1).translateObject(0.007f, 0f, 0f);
                carPos++;
            }

            if (1250 <= carPos && carPos < 1340) {
                List<Float> temp = objectObj.get(1).getCenterPoint();
                objectObj.get(1).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                objectObj.get(1).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                objectObj.get(1).translateObject(temp.get(0), temp.get(1), temp.get(2));
            }

            if (1250 <= carPos && carPos < 2330) {
                objectObj.get(1).translateObject(0f, 0f, 0.007f);
                carPos++;
            }

            if (2330 <= carPos && carPos < 2420) {
                List<Float> temp = objectObj.get(1).getCenterPoint();
                objectObj.get(1).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                objectObj.get(1).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                objectObj.get(1).translateObject(temp.get(0), temp.get(1), temp.get(2));
            }

            if (2330 <= carPos && carPos < 2680) {
                objectObj.get(1).translateObject(-0.007f, 0f, 0f);
                carPos++;
            }

            if (2680 <= carPos && carPos < 2770) {
                List<Float> temp = objectObj.get(1).getCenterPoint();
                objectObj.get(1).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                objectObj.get(1).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                objectObj.get(1).translateObject(temp.get(0), temp.get(1), temp.get(2));
            }

            if (2680 <= carPos && carPos < 2860) {
                objectObj.get(1).translateObject(0f, 0f, -0.007f);
                carPos++;
            }

            if (carPos == 2860) {
                carPos = 0;
            }

            if (modeToggle > 0) {
                if (modeToggle == 1) {
                    List<Float> temp = objectObj.get(0).getCenterPoint();
                    camera.setPosition(temp.get(0), temp.get(1), temp.get(2));
                    camera.moveBackwards(distance);
                }

                if (carPos2 < 660) {
                    objectObj.get(0).translateObject(0f, 0f, -0.01f);
                    carPos2++;
                }

                if (660 <= carPos2 && carPos2 < 750) {
                    List<Float> temp = objectObj.get(0).getCenterPoint();
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - (float) Math.toRadians(1f);
                }

                if (660 <= carPos2 && carPos2 < 1000) {
                    objectObj.get(0).translateObject(0.01f, 0f, 0f);
                    carPos2++;
                }

                if (1000 <= carPos2 && carPos2 < 1090) {
                    List<Float> temp = objectObj.get(0).getCenterPoint();
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - (float) Math.toRadians(1f);
                }

                if (1000 <= carPos2 && carPos2 < 1820) {
                    objectObj.get(0).translateObject(0f, 0f, 0.01f);
                    carPos2++;
                }

                if (1820 <= carPos2 && carPos2 < 1910) {
                    List<Float> temp = objectObj.get(0).getCenterPoint();
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - (float) Math.toRadians(1f);
                }

                if (1820 <= carPos2 && carPos2 < 2160) {
                    objectObj.get(0).translateObject(-0.01f, 0f, 0f);
                    carPos2++;
                }

                if (2160 <= carPos2 && carPos2 < 2250) {
                    List<Float> temp = objectObj.get(0).getCenterPoint();
                    objectObj.get(0).translateObject(-temp.get(0), -temp.get(1), -temp.get(2));
                    objectObj.get(0).rotateObject(-(float) Math.toRadians(1f), 0f, 1f, 0f);
                    objectObj.get(0).translateObject(temp.get(0), temp.get(1), temp.get(2));
                    angle = angle - (float) Math.toRadians(1f);
                }

                if (2160 <= carPos2 && carPos2 < 2320) {
                    objectObj.get(0).translateObject(0f, 0f, -0.01f);
                    carPos2++;
                }

                if (carPos2 == 2320) {
                    carPos2 = 0;
                }
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

            for (Object object: objectOuterWall){
                object.draw(camera, projection);
            }

            for (Object object: objectLighthouse){
                object.draw(camera, projection);
            }

            for (Object object: objectPagar){
                object.draw(camera, projection);
            }

            for (Object object: objectFinishLine){
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
