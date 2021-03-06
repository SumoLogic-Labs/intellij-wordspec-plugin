package com.sumologic.intellij.wordspec.plugin;

import com.intellij.execution.lineMarker.ExecutorAction;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.icons.AllIcons;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class WordSpecLineMarkerContributor extends RunLineMarkerContributor {
  @Override
  public @Nullable Info getInfo(@NotNull PsiElement element) {
    WordSpecExpression expression = WordSpecExpression.fromLeafIdentifier(element);
    if (expression != null) {
      Function<PsiElement, String> tooltipProvider = it -> expression.toSentence();
      return new Info(AllIcons.RunConfigurations.TestState.Run, ExecutorAction.getActions(1), tooltipProvider) {
        @Override
        public boolean shouldReplace(@NotNull Info other) {
          return true;
        }
      };
    }
    return null;
  }
}
