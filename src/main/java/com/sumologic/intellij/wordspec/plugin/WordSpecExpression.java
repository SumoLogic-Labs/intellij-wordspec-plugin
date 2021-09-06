package com.sumologic.intellij.wordspec.plugin;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

import java.util.*;

public class WordSpecExpression {
  private final static Set<String> WORD_SPEC_NODES = new HashSet<String>(
    Arrays.asList("when", "should", "must", "can", "which")
  );
  private final static Set<String> WORD_SPEC_LEAVES = new HashSet<String>(
    Arrays.asList("in")
  );

  public final PsiElement element;
  public final String subject;
  public final String word;
  public final boolean isNode;
  public final boolean isLeaf;

  public WordSpecExpression(PsiElement element) {
    assert element.toString() == "InfixExpression";

    PsiElement[] children = element.getChildren();

    assert children.length >= 3;

    PsiElement firstChild = children[0];
    PsiElement secondChild = children[1];
    PsiElement lastChild = children[children.length - 1];

    assert firstChild.toString() == "StringLiteral";
    assert secondChild.toString().startsWith("ReferenceExpression");
    assert lastChild.toString() == "BlockExpression";

    this.element = element;
    this.subject = firstChild.getText().replaceAll("^.|.$", "");
    this.word = secondChild.getText();
    this.isNode = WORD_SPEC_NODES.contains(word);
    this.isLeaf = WORD_SPEC_LEAVES.contains(word);

    assert isNode ^ isLeaf;
  }

  public static WordSpecExpression apply(PsiElement element) {
    try {
      return new WordSpecExpression(element);
    } catch(AssertionError e) {
      return null;
    }
  }

  public static WordSpecExpression fromLeafIdentifier(PsiElement element) {
    if (! (element instanceof LeafPsiElement)) { return null; }
    if (! ((LeafPsiElement) element).getElementType().toString().equals("identifier")) { return null; }
    if (element.getParent() == null || element.getParent().getParent() == null) { return null; }
    return apply(element.getParent().getParent());
  }

  public String toString() {
    if (isLeaf) {
      return subject;
    } else {
      return subject + " " + word;
    }
  }

  public String toSentence() {
    List<String> words = new ArrayList<String>();

    if (isNode) { words.add("*"); }
    words.add(toString());

    PsiElement next = element;
    while (next.getParent() != null) {
      next = next.getParent();
      WordSpecExpression expression = WordSpecExpression.apply(next);
      if (expression != null) {
        words.add(expression.toString());
      }
    }
    Collections.reverse(words);
    return String.join(" ", words);
  }
}
