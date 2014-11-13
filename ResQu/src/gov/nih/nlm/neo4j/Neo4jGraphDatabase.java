package gov.nih.nlm.neo4j;

/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jGraphDatabase
{
    private static final String DB_PATH = "target/neo4j-hello-db";

    public String greeting;
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
    private static enum RelTypes implements RelationshipType
    {
        KNOWS
    }

    public static void main( final String[] args ) throws IOException
    {
        Neo4jGraphDatabase hello = new Neo4jGraphDatabase();
        hello.createDb();
        hello.removeData();
        hello.shutDown();
    }

    void createDb() throws IOException
    {
        deleteFileOrDirectory( new File( DB_PATH ) );
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        try ( Transaction tx = graphDb.beginTx() )
        {
        	
        	BufferedReader bf = new BufferedReader(new FileReader("file.txt"));
        	String line;
        	while ((line= bf.readLine())!=null){
        		
        		line = bf.readLine();
        		
        	}
            firstNode = graphDb.createNode();
            firstNode.setProperty( "message", "Aspirin " );
            secondNode = graphDb.createNode();
            secondNode.setProperty( "message", "Migraine Disorders" );
            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "TREATS " );
            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );

            greeting = ( (String) firstNode.getProperty( "message" ) )
                       + ( (String) relationship.getProperty( "message" ) )
                       + ( (String) secondNode.getProperty( "message" ) );

            tx.success();
        }
    }

    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();

            tx.success();
        }
    }

    void shutDown()
    {
        System.out.println();
        graphDb.shutdown();
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }

    private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    }
}