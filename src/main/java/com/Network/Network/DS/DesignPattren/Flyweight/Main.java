package com.Network.Network.DS.DesignPattren.Flyweight;

public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        editor.formatText("hello world", "uppercase");
        editor.formatText("This IS an ExamPLE", "lowercase");
        editor.formatText("iNput vaLUE", "capitalize");
    }
}
