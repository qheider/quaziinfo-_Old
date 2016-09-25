package org.domain.quaziinfo.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;

import org.domain.quaziinfo.entity.dictionaryInfo;

@Name("dictionaryInfoHome")
public class dictionaryInfoHome extends EntityHome<dictionaryInfo>
{
    @RequestParameter Long dictionaryInfoId;

    @Override
    public Object getId()
    {
        if (dictionaryInfoId == null)
        {
            return super.getId();
        }
        else
        {
            return dictionaryInfoId;
        }
    }

    @Override @Begin
    public void create() {
        super.create();
    }

}
