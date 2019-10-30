import sqlite3
from pathlib import Path
import inspect

database = "schedule.db"

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
        QMString = ','.join(['?'] * len(instanceFields))
        myCursor.execute('INSERT INTO {} ({}) VALUES ({})'.format(self.table, columnNames, QMString), tuple(values))

    def delete(self, **keyValues):

        columnNames = keyValues.keys();
        values = list(keyValues.values())
        conditionString = ' AND'.join([c + '=?' for c in columnNames])

        myCursor = self.dbConnection.cursor()
        myCursor.execute('DELETE FROM {} WHERE {}'.format(self.table, conditionString), values)

    def update(self, setToValues, conditions):
        columnsToSet = list(setToValues.keys())
        valuesToSet = list(setToValues.values())
        conditionColumns = conditions.keys()
        conditionValues = list(conditions.values())
        allValues = list(valuesToSet) + list(conditionValues)

        conditionString = ' AND '.join([(conditionName + ' = ?') for conditionName in conditionColumns])
        setString = ', '.join([column + ' = ?' for column in columnsToSet])

        myCursor = self.dbConnection.cursor()
        statement = 'UPDATE {} SET {} WHERE ({})'.format(self.table, setString, conditionString)
        myCursor.execute(statement,allValues)

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


def get_type(char):
    return {
        'C': Course,
        'R': Classroom,
        'S': Student
    }[char]


def __main__():
    dbFile = Path(database)
    if not dbFile.is_file():
        exit(0)

    # connect to database
    try:
        dbConnection = sqlite3.connect(database)
    except sqlite3.Error as e:
        print(e)

    studentDao = Dao(Student, dbConnection)
    students = studentDao.find_all()

    coursesDao = Dao(Course, dbConnection)
    courses = coursesDao.find_all()

    classrommsDao = Dao(Classroom, dbConnection)
    classromms = classrommsDao.find_all()

    roomVsCourses = {room: [ course for course in courses if course.classroom_id == room.id] for room in classromms}
    for courseList in roomVsCourses.values():
        courseList.reverse()


    i = 0

    while meetConditions(dbConnection): #some conditions are met

        for room in classromms:


            if room.current_course_time_left > 0:

                currentCourse = None
                for course in roomVsCourses[room]:
                    if course.id == room.current_course_id:
                        currentCourse = course
                room.current_course_time_left = room.current_course_time_left - 1
                if room.current_course_time_left != 0:
                    print("(" + str(i) + ") " + str(room.location) + ": occupied by " + currentCourse.course_name)
                classrommsDao.update(vars(room), {"id": str(room.id)})
                if room.current_course_time_left == 0:
                    #Room was freed comment
                    print("("+str(i)+") " + room.location + ": " +str(currentCourse.course_name) + " is done")
                    coursesDao.delete(id=course.id)
                    roomVsCourses[room].remove(currentCourse)
                    if(len(roomVsCourses[room]) == 0):
                        room.current_course_id = 0
                        room.current_course_time_left = 0
                        classrommsDao.update(vars(room), {"id": room.id})
                        continue

            if room.current_course_time_left == 0:
                if len(roomVsCourses[room]) > 0:
                    nextCourse = roomVsCourses[room].pop()
                    roomVsCourses[room].append(nextCourse)
                    # Room is already free
                    print("(" + str(i) + ") " + room.location + ": " + str(nextCourse.course_name) + " is schedule to start")

                    studentName = nextCourse.student
                    for student in students:
                        if student.grade == studentName:
                            student.count = student.count - nextCourse.number_of_students
                            studentDao.update({"count": student.count}, {"grade": studentName})
                    room.current_course_id = nextCourse.id
                    room.current_course_time_left = nextCourse.course_length
                    classrommsDao.update(
                        {"current_course_id": nextCourse.id, "current_course_time_left": nextCourse.course_length},
                        {"id": room.id})

        dbConnection.commit()
        for obj in ['C', 'R', 'S']:
            Dao(get_type(obj), dbConnection).printSelf()
        i = i + 1
    dbConnection.close()



def meetConditions(connection):
    if(Path(database).is_file()):
         return connection.cursor().execute("SELECT * FROM courses").fetchone() is not None
    return False

if __name__ == '__main__':
    __main__()


