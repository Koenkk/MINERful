/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minerful.concept.constraint.relation;

import minerful.concept.TaskChar;
import minerful.concept.TaskCharSet;
import minerful.concept.constraint.Constraint;

public class NotSuccession extends NotChainSuccession {
	@Override
	public String getRegularExpressionTemplate() {
		return "[^%1$s]*(%1$s[^%2$s]*)*[^%1$s^%2$s]*";
	}

    public NotSuccession(TaskChar param1, TaskChar param2) {
        super(param1, param2);
    }
    public NotSuccession(TaskChar param1, TaskChar param2, double support) {
        super(param1, param2, support);
    }
    public NotSuccession(TaskCharSet param1, TaskCharSet param2, double support) {
		super(param1, param2, support);
	}
	public NotSuccession(TaskCharSet param1, TaskCharSet param2) {
		super(param1, param2);
	}

	@Override
    public int getHierarchyLevel() {
        return super.getHierarchyLevel()+1;
    }

    @Override
    public void setOpposedTo(RelationConstraint opposedTo) {
        super.setOpponent(opposedTo, Succession.class);
    }
    

	@Override
	public Constraint getConstraintWhichThisShouldBeBasedUpon() {
		return new NotChainSuccession(base, implied);
	}

	@Override
	public Constraint getSupposedOpponentConstraint() {
		return new Succession(base, implied);
	}
}