package de.plushnikov.intellij.plugin.extension;

import com.intellij.ide.structureView.StructureViewExtension;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.java.JavaClassTreeElement;
import com.intellij.ide.structureView.impl.java.PsiFieldTreeElement;
import com.intellij.ide.structureView.impl.java.PsiMethodTreeElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import de.plushnikov.intellij.plugin.psi.LombokLightClass;
import de.plushnikov.intellij.plugin.psi.LombokLightClassBuilder;
import de.plushnikov.intellij.plugin.psi.LombokLightFieldBuilder;
import de.plushnikov.intellij.plugin.psi.LombokLightMethod;
import de.plushnikov.intellij.plugin.psi.LombokLightMethodBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Extension to populate StructureView with all fields, methods and inner classes generated by lombok
 */
public class LombokStructureViewExtension implements StructureViewExtension {
  @Override
  public StructureViewTreeElement[] getChildren(PsiElement parent) {
    if (!(parent instanceof PsiClass)) {
      return StructureViewTreeElement.EMPTY_ARRAY;
    }

    Collection<StructureViewTreeElement> result = new ArrayList<StructureViewTreeElement>();
    final PsiClass psiClass = (PsiClass) parent;

    for (PsiField psiField : psiClass.getFields()) {
      if (psiField instanceof LombokLightFieldBuilder) {
        result.add(new LombokFieldTreeElement(psiField));
      }
    }

    for (PsiMethod psiMethod : psiClass.getMethods()) {
      if (psiMethod instanceof LombokLightMethodBuilder || psiMethod instanceof LombokLightMethod) {
        result.add(new LombokMethodTreeElement(psiMethod));
      }
    }

    for (PsiClass psiInnerClass : psiClass.getInnerClasses()) {
      if (psiInnerClass instanceof LombokLightClassBuilder || psiInnerClass instanceof LombokLightClass) {
        result.add(new LombokClassTreeElement(psiInnerClass, psiClass));
      }
    }

    if (!result.isEmpty()) {
      return result.toArray(new StructureViewTreeElement[result.size()]);
    } else {
      return StructureViewTreeElement.EMPTY_ARRAY;
    }
  }

  @Nullable
  @Override
  public Object getCurrentEditorElement(Editor editor, PsiElement parent) {
    return null;
  }

  private static class LombokFieldTreeElement extends PsiFieldTreeElement {
    public LombokFieldTreeElement(PsiField psiField) {
      super(psiField, false);
    }

    @Override
    public Icon getIcon(boolean open) {
      return IconLoader.getIcon("/icons/nodes/lombokField.png", getClass());
    }
  }

  private static class LombokMethodTreeElement extends PsiMethodTreeElement {
    public LombokMethodTreeElement(PsiMethod psiMethod) {
      super(psiMethod, false);
    }

    @Override
    public Icon getIcon(boolean open) {
      return IconLoader.getIcon("/icons/nodes/lombokMethod.png", getClass());
    }
  }

  private static class LombokClassTreeElement extends JavaClassTreeElement {
    public LombokClassTreeElement(PsiClass psiInnerClass, PsiClass psiClass) {
      super(psiInnerClass, false);
    }

    @Override
    public Icon getIcon(boolean open) {
      return IconLoader.getIcon("/icons/nodes/lombokClass.png", getClass());
    }
  }
}
