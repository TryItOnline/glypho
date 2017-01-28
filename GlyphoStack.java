// This software is Public Domain.

import java.util.Stack;

public class GlyphoStack {
  protected Stack stack;

  public GlyphoStack() {
    stack = new Stack();
  }

  public GlyphoStack(Stack s) {
    stack = s;
  }

  public void clear() {
    stack.clear();
  }

  public boolean empty() {
    return stack.empty();
  }

  public int peek() {
    return ((Integer)stack.peek()).intValue();
  }

  public int pop() {
    return ((Integer)stack.pop()).intValue();
  }

  public int push(int value) {
    return ((Integer)stack.push(new Integer(value))).intValue();
  }

  public int popBottom() {
    return ((Integer)stack.remove(0)).intValue();
  }

  public void pushBottom(int value) {
    stack.add(0, new Integer(value));
  }

  public String toString() {
    return stack.toString();
  }

}