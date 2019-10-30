import sqlite3
from pathlib import Path
import sys
import inspect


# DTOs
class Course:
    def __init__(self, id, course_name, student, number_of_students, classroom_id, course_length):
        self.id = int(id)
        self.course_name = course_name
        self.student = student
        self.number_of_students = int(number_of_students)
        self.classroom_id = int(classroom_id)
        self.course_length = int(course_length)

class Student:
    def __init__(self, grade, count):
        self.grade = grade
        self.count = int(count)

class Classroom:
    def __init__(self, id, location):
        self.id = id
        self.location = location
        self.current_course_id = 0  # default value
        self.current_course_time_left = 0  # default value


# DAOs
class Dao:
    def __init__(self, DTOtype, dbConnection):
        self.dbConnection = dbConnection
        self.DTOtype = DTOtype
        self.table = DTOtype.__name__.lower() + "s"

    def printSelf(self):
        myCursor = self.dbConnection.cursor()
        myCursor.execute('SELECT * FROM {}'.format(self.table))
        print(self.table)
        for something in myCursor.fetchall():
            print(something)

    def insert(self, DTOInstance):
        myCursor = self.dbConnection.cursor()
        instanceFields = vars(DTOInstance)
        columnNames = ','.join(instanceFields.keys());
        values = instanceFields.values();
        qString = ','.join(['?'] * len(instanceFields))
        myCursor.execute('INSERT INTO {} ({}) VALUES ({})'.format(self.table, columnNames, qString), tuple(values))

    def delete(self, **keyValues):

        columnNames = keyValues.keys();
        values = list(keyValues.values())
        conditionString = ' AND'.join([c + '=?' for c in columnNames])

        myCursor = self.dbConnection.cursor()
        myCursor.execute('DELETE FROM {} WHERE {}'.format(self.table, conditionString), values)

    def update(self, setToValues, conditions):
        columnsToSet = list(setToValues.keys())
        valuesToSet = list(setToValues.values())
        conditionColumns = list(conditions.keys())
        conditionValues = list(conditions.values())
        allValues = list(valuesToSet) + list(conditionValues)

        conditionString = ' AND'.join([(conditionName + ' =?') for conditionName in columnsToSet])
        setString = ', '.join([column + ' = ?' for column in columnsToSet])

        myCursor = self.dbConnection.cursor()
        myCursor.execute('UPDATE {} SET {} WHERE{}'.format(self.table, setString, conditionString ), allValues)

    def find(self, **keysValues):
        columnsNames = keysValues.keys()
        values = keysValues.values()
        conditionString = ' AND'.join([column + '=?' for column in columnsNames])

        myCursor = self.dbConnection.cursor()
        myCursor.execute("SELECT * FROM {} WHERE {}".format(self.table, conditionString), values)

    def find_all(self):
        mycursor = self.dbConnection.cursor()
        mycursor.execute("SELECT * FROM {}".format(self.table))
        return orm(mycursor, self.DTOtype)

def orm(cursor, dto_type):
    args = inspect.getfullargspec(dto_type.__init__).args[1:]

    col_names = [column[0] for column in cursor.description]

    col_mapping = [col_names.index(arg) for arg in args]

    return [row_map(row, col_mapping, dto_type) for row in cursor.fetchall()]

def row_map(row, col_mapping, dto_type):
    ctor_args = [row[idx] for idx in col_mapping]
    return dto_type(*ctor_args)

def createStudents(connection):
    connection.cursor().execute("""CREATE TABLE students (
                                    grade TEXT PRIMARY KEY,
                                    count INTEGER NOT NULL
                                )""")
def createCourses(connection):
    connection.cursor().execute("""CREATE TABLE courses (
                                        id INTEGER PRIMARY KEY,
                                        course_name TEXT NOT NULL,
                                        student TEXT NOT NULL,
                                        number_of_students INTEGER NOT NULL,
                                        classroom_id REFERENCES classrooms (id),
                                        course_length INTEGER NOT NULL
                                    )""")


def createClassrooms(connection):
    connection.cursor().execute("""CREATE TABLE classrooms (
                                        id INT PRIMARY KEY,
                                        location TEXT NOT NULL,
                                        current_course_id INT NOT NULL ,
                                        current_course_time_left INT NOT NULL 
                                    )""")

def get_type(char):
    return {
        'C': Course,
        'R': Classroom,
        'S': Student
    }[char]

def __main__():
    configFile = sys.argv[1]
    dbFileName = "schedule.db"
    dbFile = Path(dbFileName)

    if dbFile.is_file():
        exit(0)

    try:
        dbConnection = sqlite3.connect(dbFileName)
    except sqlite3.Error as e:
        print(e)
        print("Can't connect to db")

    createCourses(dbConnection)
    createStudents(dbConnection)
    createClassrooms(dbConnection)

    with open(configFile) as file:
        fileContent = file.readlines()

    fileContent = [l.strip() for l in fileContent]
    for line in fileContent:
        if line:
            splitLine = line.split(", ")
            odt = row_map(splitLine[1:], range(len(splitLine) - 1), get_type(splitLine[0]))
            dao = Dao(get_type(splitLine[0]), dbConnection)
            dao.insert(odt)

    for type in ['C', 'R', 'S']:
        Dao(get_type(type), dbConnection).printSelf()

    dbConnection.commit()
    dbConnection.close()

if __name__ == '__main__':
    __main__()







