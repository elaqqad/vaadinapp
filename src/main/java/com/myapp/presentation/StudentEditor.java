package com.myapp.presentation;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import javax.inject.Inject;
import com.myapp.entities.Student;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import com.myapp.presentation.EntityEvent.Type;

public class StudentEditor extends Window implements Button.ClickListener {

    /* The entity edited by this class */
    private Student student;

    private final MTextField name = new MTextField("Name");
    private final MTextField address = new MTextField("Address");
    private final MTextField age = new MTextField("Age").withConverter(new StringToIntegerConverter());

    /* Action buttons */
    private final Button cancel = new Button("Cancel", this);
    // PrimaryButton reacts hitting on ENTER
    private final Button save = new PrimaryButton("Save", this);

    public StudentEditor() {
        setCaption("Edit Student:");
        setModal(true);
        setContent(new MVerticalLayout(name, age, address, new MHorizontalLayout(save,
                cancel).withMargin(false)));
    }

    public void setStudent(Student student) {
        this.student = student;
        BeanBinder.bind(student, this);
        name.selectAll();
    }

    @Override
    public void buttonClick(Button.ClickEvent ce) {
        /* Fire relevant CDI event on button clicks */
        if (ce.getButton() == save) {
            saveEvent.fire(student);
        } else {
            refrehsEvent.fire(student);
        }
    }

    /* "CDI interface" to notify decoupled components. Using traditional API to 
     * other componets would probably be easier in this small app, but just 
     * demonstrating here how all CDI stuff is available for Vaadin apps. 
     */
    
    @Inject
    @EntityEvent(Type.SAVE)
    javax.enterprise.event.Event<Student> saveEvent;

    @Inject
    @EntityEvent(Type.REFRESH)
    javax.enterprise.event.Event<Student> refrehsEvent;

}
