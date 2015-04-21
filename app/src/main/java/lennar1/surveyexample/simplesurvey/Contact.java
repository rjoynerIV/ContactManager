package lennar1.surveyexample.simplesurvey;



/**
 * Created by Robert Joyner on 4/13/15.
 */
public class Contact {

    private String _txtFName, _txtLName, _txtPhone, _txtEmail, _txtZip;
    private int _idContact;


    public Contact (int idContact, String txtFName,String txtLName,String txtEmail, String txtPhone,
                    String txtZip) {
        _idContact = idContact;
        _txtFName = txtFName;
        _txtLName = txtLName;
        _txtEmail = txtEmail;
        _txtPhone = txtPhone;
        _txtZip = txtZip;

    }

    public int getidContact () {return _idContact;}

    public String gettxtFName () {return _txtFName;}

    public String gettxtLName () {return _txtLName;}

    public String gettxtEmail () {return _txtEmail;}

    public String gettxtPhone () {return _txtPhone;}

    public String gettxtZip () {return _txtZip;}

}
