package com.myapp.presentation;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import javax.inject.Inject;
import com.myapp.entities.Project;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import com.myapp.presentation.EntityEvent.Type;

public class ProjectEditor extends Window implements Button.ClickListener {

    /* The entity edited by this class */
    private Project project;

    private final MTextField title = new MTextField("Title");

    /* Action buttons */
    private final Button cancel = new Button("Cancel", this);
    // PrimaryButton reacts hitting on ENTER
    private final Button save = new PrimaryButton("Save", this);

    public ProjectEditor() {
        setCaption("Add Project:");
        setModal(true);
        setContent(new MVerticalLayout(title, new MHorizontalLayout(save,
                cancel).withMargin(false)));
    }

    public void setProject(Project project) {
        this.project = project;
        BeanBinder.bind(project, this);
        title.selectAll();
    }

    @Override
    public void buttonClick(Button.ClickEvent ce) {
        /* Fire relevant CDI event on button clicks */
        if (ce.getButton() == save) {
            saveEvent.fire(project);
        } else {
            refrehsEvent.fire(project);
        }
    }

    /* "CDI interface" to notify decoupled components. Using traditional API to 
     * other componets would probably be easier in this small app, but just 
     * demonstrating here how all CDI stuff is available for Vaadin apps. 
     */
    
    @Inject
    @EntityEvent(Type.SAVE)
    javax.enterprise.event.Event<Project> saveEvent;

    @Inject
    @EntityEvent(Type.REFRESH)
    javax.enterprise.event.Event<Project> refrehsEvent;

}
