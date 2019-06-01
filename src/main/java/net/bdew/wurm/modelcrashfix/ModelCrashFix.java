package net.bdew.wurm.modelcrashfix;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

public class ModelCrashFix implements WurmClientMod, PreInitable {
    @Override
    public void preInit() {
        ClassPool cp = HookManager.getInstance().getClassPool();
        try {
            cp.getCtClass("com.wurmonline.client.renderer.cell.MobileModelRenderable")
                    .getMethod("overrideMaskTextures", "()V")
                    .insertBefore(
                            "                if (model != null && model.isLoaded() && !(getModelWrapper().getModelData() instanceof com.wurmonline.client.renderer.model.collada.ColladaModelData)) {" +
                                    "            java.lang.System.out.println(java.lang.String.format(\"Warning: Mobile item %s (%d) uses static model (%s) but has color assigned from server. This will crash vanilla clients!\"," +
                                    "                    new Object[]{getHoverName(), new java.lang.Long(getId()), getModelName().toString()}));" +
                                    "            return;" +
                                    "        }");
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
