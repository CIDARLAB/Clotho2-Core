/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.hibernate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.clothocad.hibernate.data.PlasmidTable;
import org.clothocore.api.data.*;
import org.clothocore.core.Hub;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author sixpi
 */
public class HibernateConnectionTest {
    private SessionFactory fac;
    
    public HibernateConnectionTest() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
    @Test
    public void test() {
        HibernateConnection conn = new HibernateConnection();
        
        FileObject configFile;
        configFile =
                FileUtil.getConfigFile( "data/private/org.clothocad.testdata/" )
                .getFileObject("test.hbm.xml"); // Change database params in here
        assertNotNull(configFile, "Could not find hbm file");
        
        List<URL> mappings = new ArrayList<URL>();
        List<FileObject> mappingfiles = HibernateConnection.getDefaultMappings();
        assertNotNull(mappingfiles);
        for ( FileObject map : mappingfiles) {
            mappings.add(map.toURL());
        }
        
        Configuration config = new Configuration();
        for ( URL map : mappings ) {
            config.addURL( map );
        }

        try {
            // Attempt to create a session factory from the configuration.
            // As long as this session factory is active, the connection is
            // defined as active.
            config.configure( configFile.toURL() );
            fac = config.buildSessionFactory();
        } catch ( Throwable e ) {
            System.err.println( e );
            fac = null;
        }
        Session openSession = fac.openSession();
        // Change this to the id for plasmid you want to delete
        PlasmidTable p = (PlasmidTable) openSession.load(PlasmidTable.class, "0cd793fcd026457cb7031a5efd2c868c");
        assertNotNull(p, "Could not get plasmid");

        Transaction beginTransaction = openSession.beginTransaction();
        openSession.delete(p);
        beginTransaction.commit();
        openSession.close();
        
        //assertTrue(conn.connect(configFile.toURL(), mappings), "Could not connect to test database");
        // Would like the following to create a new plasmid and sample in the database to delete
        // but will just get an existing one for now.
        //        Part pt = (Part) conn.get(ObjType.PART, "f3b03fc137a74c0cbbd32518d136baa3");
        //        Vector vec = (Vector) conn.get(ObjType.VECTOR, "cf42bd792c1844f9b9a998c151138e6f");
        //        Person author = (Person) conn.get(ObjType.PERSON, "31f28426be0e45639a827359aa7b9a5f");
        //        Format form = (Format) conn.get(ObjType.FORMAT, "org-clothocad-format-freeform-connect");
        //        assertNotNull(pt, "Could not find part");
        //        assertNotNull(vec, "Could not find vector");
        //        assertNotNull(author, "Could not find author");
        //        assertNotNull(form, "Could not find format");
        //Plasmid plasm;
        //plasm = (Plasmid) conn.get(ObjType.PLASMID, "0cd793fcd026457cb7031a5efd2c868c");
        //ArrayList<Sample> samples = plasm.getSamples();
        //assertNotNull(plasm, "Could not get plasmid");
        //assertTrue(conn.delete(plasm), "Could not delete");
    }
}
