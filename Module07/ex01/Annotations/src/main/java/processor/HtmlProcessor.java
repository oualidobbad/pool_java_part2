package processor;

import com.google.auto.service.AutoService;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import java.io.Writer;
import java.util.Set;

import annotations.HtmlForm;
import annotations.HtmlInput;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({"annotations.HtmlForm", "annotations.HtmlInput"})
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // Debug: confirm that process is called
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "HtmlProcessor: process() called"
        );

        for (Element e : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            TypeElement tE = (TypeElement) e;
            HtmlForm htmlForm = tE.getAnnotation(HtmlForm.class);

            StringBuilder html = new StringBuilder("<form action=\"");
            html.append(htmlForm.action());
            html.append("\" method=\"");
            html.append(htmlForm.method());
            html.append("\">\n");

            for (Element enclosed : tE.getEnclosedElements()) {
                HtmlInput htmlInput = enclosed.getAnnotation(HtmlInput.class);
                if (htmlInput == null) continue;

                html.append("<input type=\"");
                html.append(htmlInput.type());
                html.append("\" name=\"");
                html.append(htmlInput.name());
                html.append("\" placeholder=\"");
                html.append(htmlInput.placeholder());
                html.append("\">\n");
            }

            html.append("<input type=\"submit\" value=\"Send\">");
            html.append("</form>");

            try {
                FileObject fileObject = processingEnv.getFiler().createResource(
                        StandardLocation.CLASS_OUTPUT,
                        "",
                        htmlForm.fileName()
                );
                try (Writer writer = fileObject.openWriter()) {
                    writer.write(html.toString());
                }
            } catch (Exception ex) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Error generating HTML: " + ex.getMessage(),
                        e
                );
            }
        }
        return true;
    }
}