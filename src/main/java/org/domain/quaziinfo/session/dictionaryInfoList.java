package org.domain.quaziinfo.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.domain.quaziinfo.entity.dictionaryInfo;

@Name("dictionaryInfoList")
public class dictionaryInfoList extends EntityQuery<dictionaryInfo>
{
    public dictionaryInfoList()
    {
        setEjbql("select dictionaryInfo from dictionaryInfo dictionaryInfo");
    }
}
