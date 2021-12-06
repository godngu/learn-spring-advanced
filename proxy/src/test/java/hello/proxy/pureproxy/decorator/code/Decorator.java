package hello.proxy.pureproxy.decorator.code;

public abstract class Decorator implements Component {

    private final Component component;

    protected Decorator(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
