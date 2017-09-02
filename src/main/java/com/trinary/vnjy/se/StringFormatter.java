/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.se;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.python.core.PyString;

/**
 *
 * @author mmain
 */
public class StringFormatter {
	protected ScriptEngine se;

	public StringFormatter(ScriptEngine se) {
		this.se = se;
	}

	public PyString format(PyString string) {
		String s = string.toString();
		Pattern p = Pattern.compile("#\\{([^{]*)\\}");
		Matcher m = p.matcher(s);

		while (m.find()) {
			PyString item = new PyString(m.group(1));
			PyString value = (PyString) se.getNamespace().get(item);
			s = s.replaceAll("#\\{" + item.toString() + "\\}", value.toString());
		}

		return new PyString(s);
	}

	public String format(String s) {
		Pattern p = Pattern.compile("#\\{([^{]*)\\}");
		Matcher m = p.matcher(s);

		while (m.find()) {
			PyString item = new PyString(m.group(1));
			PyString value = (PyString) se.getNamespace().get(item);
			s = s.replaceAll("#\\{" + item.toString() + "\\}", value.toString());
		}

		return s;
	}
}
