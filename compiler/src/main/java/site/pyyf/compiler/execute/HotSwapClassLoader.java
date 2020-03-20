package site.pyyf.compiler.execute;

import java.util.Map;

public class HotSwapClassLoader extends ClassLoader {
    private Map<String, byte[]> modifiedByte;

    public HotSwapClassLoader(Map<String, byte[]> modifiedByte) {
        super(HotSwapClassLoader.class.getClassLoader());
        this.modifiedByte = modifiedByte;
    }

    @Override
    protected Class<?> findClass(String name) {
//        System.out.println("加载了" + name);
        return defineClass(name, modifiedByte.get(name), 0, modifiedByte.get(name).length);
    }
}
