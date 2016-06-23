package com.javarush.test.level32.lesson15.big01;

import com.javarush.test.level32.lesson15.big01.listeners.FrameListener;
import com.javarush.test.level32.lesson15.big01.listeners.TabbedPaneChangeListener;
import com.javarush.test.level32.lesson15.big01.listeners.UndoListener;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vadym on 03.06.2016.
 */
public class View extends JFrame implements ActionListener
{
    private Controller controller;
    //это будет панель с двумя вкладками
    private JTabbedPane tabbedPane = new JTabbedPane();
    //это будет компонент для визуального редактирования html
    private JTextPane htmlTextPane = new JTextPane();
    //это будет компонент для редактирования html в виде текста, он будет отображать код html (теги и их содержимое)
    private JEditorPane plainTextPane = new JEditorPane();

    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);
    public View()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }
    public void undo()
    {
        try
        {
            undoManager.undo();
        }
        catch (CannotUndoException e)
        {
            ExceptionHandler.log(e);
        }
    }
    public void redo()
    {
        try
        {
            undoManager.redo();
        }
        catch (CannotUndoException e)
        {
            ExceptionHandler.log(e);
        }
    }

    public UndoListener getUndoListener()
    {
        return undoListener;
    }

    public void resetUndo()
    {
        undoManager.discardAllEdits();
    }

    public Controller getController()
    {
        return controller;
    }

    public void setController(Controller controller)
    {
        this.controller = controller;
    }
        public void exit()
    {
        getController().exit();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {

            case "Новый":
                controller.createNewDocument();
            case "Открыть":
                controller.openDocument();
                break;
            case "Сохранить":
                controller.saveDocument();
                break;
            case "Сохранить как...":
               controller.saveDocumentAs();
                break;
            case "Выход":
               controller.exit();
                break;
            case "О программе":
                showAbout();
                break;
        }
    }
    public void initMenuBar()
    {
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }

    public void initEditor()
    {
        htmlTextPane.setContentType("text/html");
        JScrollPane SP = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", SP);
        JScrollPane SP2 = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", SP2);
        tabbedPane.setPreferredSize(new Dimension(800, 800));
        TabbedPaneChangeListener tPCL = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tPCL);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

    }
    public void initGui()
    {
        initMenuBar();
        initEditor();
        pack();
    }
    public void init()
    {
        initGui();
        FrameListener fl = new FrameListener(this);
        addWindowListener(fl);
        setVisible(true);

    }

    public void selectedTabChanged()
    {
        if (tabbedPane.getSelectedIndex() == 0)
        {
            controller.setPlainText(plainTextPane.getText());
        }
        else
        {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }

    public boolean isHtmlTabSelected()
    {
        return tabbedPane.getSelectedIndex() == 0;
    }

    public boolean canUndo()
    {
        return undoManager.canUndo();
    }

    public boolean canRedo()
    {
        return undoManager.canRedo();
    }
 public void selectHtmlTab()
 {
     tabbedPane.setSelectedIndex(0);
     resetUndo();
 }
    public void update()
    {
        Document doc = controller.getDocument();
        htmlTextPane.setDocument(doc);
    }
    public void showAbout() {
        JOptionPane.showMessageDialog(this, "HTML Editor", "About", JOptionPane.INFORMATION_MESSAGE);
    }


}
