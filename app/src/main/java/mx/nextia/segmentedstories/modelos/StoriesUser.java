package mx.nextia.segmentedstories.modelos;

import java.util.ArrayList;

public class StoriesUser {

    ArrayList<Story> al_historias;
    int id;

    public StoriesUser(ArrayList<Story> al_historias, int id) {
        this.al_historias = al_historias;
        this.id = id;
    }

    public ArrayList<Story> getAl_historias() {
        return al_historias;
    }

    public int getId() {
        return id;
    }

    public void setAl_historias(ArrayList<Story> al_historias) {
        this.al_historias = al_historias;
    }

    public void setId(int id) {
        this.id = id;
    }
}
