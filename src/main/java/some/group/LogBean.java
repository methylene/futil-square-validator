package some.group;

import static com.google.common.collect.Iterables.transform;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import com.google.common.base.Function;
import com.sun.faces.facelets.compiler.UIInstructions;

@ManagedBean
@RequestScoped
public class LogBean {
	
	public static Function<Validator, String> C = new Function<Validator, String>() {
		public String apply(Validator uic) {
			return uic.getClass().getSimpleName();
		}
	};

	
	public static Function<UIComponent, String> D = new Function<UIComponent, String>() {
		public String apply(UIComponent uic) {
			final String string = uic.toString();
			if (uic instanceof UIInput) {
				final UIInput uic2 = (UIInput) uic;
				return string + transform(Arrays.asList(uic2.getValidators()), C).toString();
			} else {
				return string;
			}
		}
	};
	
	
	public static Function<UIComponent, String> E = new Function<UIComponent, String>() {
		public String apply(UIComponent uic) {
			return transform(uic.getChildren(), D).toString();
		}
	};


	public static Function<UIComponent, String> F = new Function<UIComponent, String>() {
		public String apply(UIComponent uic) {
			if (uic instanceof UIOutput) {
				final UIOutput uic2 = (UIOutput) uic;
				return transform(uic2.getChildren(), E).toString();
			} else if (uic instanceof UIInstructions) {
				final UIInstructions uic2 = (UIInstructions) uic;
				return "UIInstructions:"+uic.toString();
			} else {
				return uic.getClass().getSimpleName();
			}
		}
	};

	public void action() {
		final FacesContext fc = getCurrentInstance();
		final UIViewRoot viewRoot = fc.getViewRoot();
		final List<UIComponent> children = viewRoot.getChildren();
		final Iterable<String> transformed = transform(children, F);
		getLogger(getClass()).info("{}: {}", viewRoot.getViewId(), transformed.toString());
	}

}
