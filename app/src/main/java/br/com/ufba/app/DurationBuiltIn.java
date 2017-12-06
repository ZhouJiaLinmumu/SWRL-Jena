package br.com.ufba.app;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.reasoner.rulesys.*;
import com.hp.hpl.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class DurationBuiltIn extends BaseBuiltin {

	public String getName() {
		return "durationDateTime";
	}

	@Override
	public int getArgLength() {
		return 4;
	}

	@Override
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		checkArgs(length, context);

		Node n1 = getArg(0, args, context);
		Node n2 = getArg(1, args, context);
		Node n3 = getArg(2, args, context);
		Node n4 = getArg(3, args, context);
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:SS.sss'Z'");
		
		String typeArgsString = n4.getLiteralValue().toString();
		int resultExpectd = Integer.parseInt(n1.getLiteralValue().toString());

		DateTime beginDate = formatter.parseDateTime(n2.getLiteralValue().toString());
		DateTime endDate = formatter.parseDateTime(n3.getLiteralValue().toString());
		
		if (typeArgsString.equalsIgnoreCase("year")) {
			return endDate.getYear() - beginDate.getYear() == resultExpectd;

		} else if (typeArgsString.equalsIgnoreCase("day")) {
			return endDate.getDayOfYear() - beginDate.getDayOfYear() == resultExpectd;

		} else if (typeArgsString.equalsIgnoreCase("month")) {
			return endDate.getMonthOfYear() - beginDate.getMonthOfYear() == resultExpectd;
		}

		return false;
	}
}
