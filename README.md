# Order Processing for World of Buzz

This is what I could do in this short time after my iMac went wrong.

## Building the project

The project is a Maven project so building is available like `mvn package`

## Running the project

After building the app can be run with this command:

```bash
$ java -jar target/orderprocessing-1.0-SNAPSHOT.jar
```

The input file will be `data.csv` and the output file will be `out.csv`.
These can be overwritten by command line arguments:

```bash
$ java -jar target/orderprocessing-1.0-SNAPSHOT.jar inputfile outputfile
```

## Configurations

The database configurations should be done in the `hibernate.cfg.xml` file.

### What's missing

- There are no unit tests
- No FTP push
- Validation is insufficient now. The rest.
