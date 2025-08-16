import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleBean {

    private final String dependencyA;
    private final String dependencyB;

    // 無參數建構子
    public ExampleBean() {
        this.dependencyA = "DefaultA";
        this.dependencyB = "DefaultB";
        System.out.println("使用無參數建構子建立 ExampleBean");
    }

    // 兩個參數的建構子
    @Autowired
    public ExampleBean(String dependencyA, String dependencyB) {
        this.dependencyA = dependencyA;
        this.dependencyB = dependencyB;
        System.out.println("使用兩個參數建構子建立 ExampleBean");
    }

    public void printDeps() {
        System.out.println("A = " + dependencyA + ", B = " + dependencyB);
    }

    ExampleBean exampleBean = new ExampleBean();
}
