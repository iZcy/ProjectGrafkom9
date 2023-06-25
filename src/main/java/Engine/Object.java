package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Object extends ShaderProgram{

    String name;
    int type;
    List<Vector3f> vertices;
    Vector3f pseudoSize;
    int vao;
    int vbo;
    UniformsMap uniformsMap;
    Vector4f color;
    List<Vector3f> curve = new ArrayList<>();
    boolean isCurve;
    float thickness;

    public Matrix4f model;

    int vboColor;

    List<Object> childObject;
    List<Float> centerPoint;

    public void setThickness(float thickness){
        this.thickness = thickness;
    }

    public float getThickness() {
        return thickness;
    }

    public List<Object> getChildObject() {
        return childObject;
    }

    public void setChildObject(List<Object> childObject) {
        this.childObject = childObject;
    }

    public List<Float> getCenterPoint() {
        updateCenterPoint();
        return centerPoint;
    }

    public void setCenterPoint(List<Float> centerPoint) {
        this.centerPoint = centerPoint;
    }

    List<Vector3f> verticesColor;
    public Object(List<ShaderModuleData> shaderModuleDataList
            , List<Vector3f> vertices
            , Vector4f color, String name, Vector3f pseudoSize, int type) {
        super(shaderModuleDataList);
        this.vertices = vertices;
//        setupVAOVBO();
        uniformsMap = new UniformsMap(getProgramId());
        uniformsMap.createUniform(
                "uni_color");
        uniformsMap.createUniform(
                "model");
        uniformsMap.createUniform(
                "projection");
        uniformsMap.createUniform(
                "view");
        uniformsMap.createUniform("dirLight.direction");
        uniformsMap.createUniform("dirLight.ambient");
        uniformsMap.createUniform("dirLight.diffuse");
        uniformsMap.createUniform("dirLight.specular");
        for(int i = 0; i < 4; i++){
            uniformsMap.createUniform("pointLight["+i+"].position");
            uniformsMap.createUniform("pointLight["+i+"].ambient");
            uniformsMap.createUniform("pointLight["+i+"].diffuse");
            uniformsMap.createUniform("pointLight["+i+"].specular");
            uniformsMap.createUniform("pointLight["+i+"].constant");
            uniformsMap.createUniform("pointLight["+i+"].linear");
            uniformsMap.createUniform("pointLight["+i+"].quadratic");
        }
        uniformsMap.createUniform("spotLight.position");
        uniformsMap.createUniform("spotLight.direction");
        uniformsMap.createUniform("spotLight.ambient");
        uniformsMap.createUniform("spotLight.diffuse");
        uniformsMap.createUniform("spotLight.specular");
        uniformsMap.createUniform("spotLight.constant");
        uniformsMap.createUniform("spotLight.linear");
        uniformsMap.createUniform("spotLight.quadratic");
        uniformsMap.createUniform("spotLight.cutOff");
        uniformsMap.createUniform("spotLight.outerCutOff");
        uniformsMap.createUniform("viewPos");
        this.color = color;
        model = new Matrix4f().identity();
        childObject = new ArrayList<>();
        centerPoint = Arrays.asList(0f,0f,0f);

        this.name = name;
        this.pseudoSize = pseudoSize;
        this.type = type;
    }
    public Object(List<ShaderModuleData> shaderModuleDataList,
                  List<Vector3f> vertices,
                  List<Vector3f> verticesColor) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.verticesColor = verticesColor;
        setupVAOVBOWithVerticesColor();
    }

    public void createEllipsoid() {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for (double v = -Math.PI / 2; v <= Math.PI / 2; v += Math.PI / 60) {
            for (double u = -Math.PI; u <= Math.PI; u += Math.PI / 60) {
                float x = 0.5f * (float) (Math.cos(v) * Math.cos(u));
                float y = 0.5f * (float) (Math.cos(v) * Math.sin(u));
                float z = 0.5f * (float) (Math.sin(v));
                temp.add(new Vector3f(x, y, z));
            }
        }
        vertices = temp;
        setupVAOVBO();
    }

    public void setupVAOVBO(){
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
    }
    public void setupVAOVBOWithVerticesColor(){
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);

        //set vboColor
        vboColor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(verticesColor),
                GL_STATIC_DRAW);
    }
    public void drawSetup(Camera camera, Projection projection){
        bind();
        uniformsMap.setUniform(
                "uni_color", color);
        uniformsMap.setUniform(
                "model", model);
        uniformsMap.setUniform(
                "view", camera.getViewMatrix());
        uniformsMap.setUniform(
                "projection", projection.getProjMatrix());
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.3f, -3f, 0f));
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.1f, 0.1f, 0.1f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(2.4f, 2.4f, 2.4f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

        Vector3f[] _pointLightPositions = {
                new Vector3f(-1f, 100f, 3f),
                new Vector3f(49f, 100f, 3f),
                new Vector3f(99f, 100f, 3f),
                new Vector3f(149f, 100f, 3f),
        };

        for(int i = 0; i < _pointLightPositions.length; i++){
            uniformsMap.setUniform("pointLight["+i+"].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLight["+i+"].ambient", new Vector3f(0.1f, 0.1f, 0.1f));
            uniformsMap.setUniform("pointLight["+i+"].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            uniformsMap.setUniform("pointLight["+i+"].specular", new Vector3f(0.5f, 0.5f, 0.5f));
            uniformsMap.setUniform("pointLight["+i+"].constant", 1.0f);
            uniformsMap.setUniform("pointLight["+i+"].linear", 0.0014f);
            uniformsMap.setUniform("pointLight["+i+"].quadratic", 0.000007f);
        }

        // spotLight
        uniformsMap.setUniform("spotLight.position", new Vector3f(0f, 0.f, 0.f));
        uniformsMap.setUniform("spotLight.direction", new Vector3f(0, -3f, 0));
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f ,0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.007f);
        uniformsMap.setUniform("spotLight.quadratic", 0.0002f);
//        uniformsMap.setUniform("spotLight.cutOff", (float)Math.cos(Math.toRadians(30f)));
//        uniformsMap.setUniform("spotLight.outerCutOff", (float)Math.cos(Math.toRadians(30f)));
        uniformsMap.setUniform("spotLight.cutOff", 0f);
        uniformsMap.setUniform("spotLight.outerCutOff", 0f);

        uniformsMap.setUniform("viewPos", camera.getPosition());

        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3,
                GL_FLOAT,
                false,
                0, 0);

    }
    public void drawSetupWithVerticesColor(){
        bind();
        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3,
                GL_FLOAT,
                false,
                0, 0);

        // Bind VBOColor
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glVertexAttribPointer(1, 3,
                GL_FLOAT,
                false,
                0, 0);
    }
    public void draw(Camera camera, Projection projection){
        drawSetup(camera, projection);
        glLineWidth(10);
        glPointSize(10);
        glDrawArrays(GL_TRIANGLES,
                0,
                vertices.size());
        for(Object child:childObject){
            child.draw(camera,projection);
        }
    }

    public void draw(Camera camera, Projection projection, String name, Vector3f settings){
        drawSetup(camera, projection);
        glLineWidth(10);
        glPointSize(10);
        glDrawArrays(GL_TRIANGLES,
                0,
                vertices.size());
        for(Object child:childObject){
            child.draw(camera,projection);
        }
    }

    public void translateObject(Float offsetX,Float offsetY,Float offsetZ){
        model = new Matrix4f().translate(offsetX,offsetY,offsetZ).mul(new Matrix4f(model));
        updateCenterPoint();
        for(Object child:childObject){
            centerPoint.set(0,centerPoint.get(0) + offsetX);
            centerPoint.set(1,centerPoint.get(1) + offsetY);
            centerPoint.set(2,centerPoint.get(2) + offsetZ);
            child.translateObject(offsetX,offsetY,offsetZ);
        }
    }

    public void rotateObject(Float degree, Float x,Float y,Float z){
        model = new Matrix4f().rotate(degree,x,y,z).mul(new Matrix4f(model));
        updateCenterPoint();
        for(Object child:childObject){
            centerPoint.set(0,centerPoint.get(0) + x);
            centerPoint.set(1,centerPoint.get(1) + y);
            centerPoint.set(2,centerPoint.get(2) + z);
            child.rotateObject(degree,x,y,z);
        }
    }
    public void updateCenterPoint(){
        Vector3f destTemp = new Vector3f();
        model.transformPosition(0.0f,0.0f,0.0f,destTemp);
        centerPoint.set(0,destTemp.x);
        centerPoint.set(1,destTemp.y);
        centerPoint.set(2,destTemp.z);
    }
    public void scaleObject(Float scaleX,Float scaleY,Float scaleZ){
        model = new Matrix4f().scale(scaleX,scaleY,scaleZ).mul(new Matrix4f(model));
        for(Object child:childObject){
            child.translateObject(scaleX,scaleY,scaleZ);
        }
    }

    private Vector3f bezierCurve(double t){
        int i = 0;
        int size = vertices.size() - 1;
        Vector3f result = new Vector3f(0.0f, 0.0f, 0.0f);
        for(Vector3f vertice : vertices){
            result.x += combinations(size, i) * Math.pow((1-t), size - i) * vertice.x * Math.pow(t, i);
            result.y += combinations(size, i) * Math.pow((1-t), size - i) * vertice.y * Math.pow(t, i);
            result.z += combinations(size, i) * Math.pow((1-t), size - i) * vertice.z * Math.pow(t, i);
            i += 1;
        }
        return result;
    }

    private int combinations(int n, int r){
        return factorial(n) / factorial(r) / factorial(n - r);
    }

    private int factorial(int n){
        int result = 1;
        for(int i = 1; i <= n; i++){
            result *= i;
        }
        return result;
    }

    public float getDepth() {
        float minZ = Float.MAX_VALUE;
        float maxZ = Float.MIN_VALUE;
        for (Vector3f vertex : vertices) {
            float z = vertex.z;
            if (z < minZ) {
                minZ = z;
            }
            if (z > maxZ) {
                maxZ = z;
            }
        }
        return maxZ - minZ;
    }

    public float getHeight() {
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        for (Vector3f vertex : vertices) {
            float y = vertex.y;
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        return maxY - minY;
    }

    public float getWidth() {
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        for (Vector3f vertex : vertices) {
            float x = vertex.x;
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
        }
        return maxX - minX;
    }

    public List<Float> getPosition() {
        List<Float> position = new ArrayList<>();
        position.add(centerPoint.get(0));
        position.add(centerPoint.get(1));
        position.add(centerPoint.get(2));
        return position;
    }

    public String getName() {
        return this.name;
    }

    public Vector3f getSize() {
        return this.pseudoSize;
    }

    public int getType() {
        return this.type;
    }
}
