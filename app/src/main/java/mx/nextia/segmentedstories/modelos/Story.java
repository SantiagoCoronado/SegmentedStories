package mx.nextia.segmentedstories.modelos;

public class Story {

    String url;
    int duration;
    int tipo;

    public Story(String url, int duration, int tipo) {
        this.url = url;
        this.duration = duration;
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        return duration;
    }

    public int getTipo() {
        return tipo;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
