let loadEmployeesBtn = document.getElementById('loadEmployees');

loadEmployeesBtn.addEventListener('click', onLoadEmployees)

function onLoadEmployees() {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

  let employeesContainer = document.getElementById('employees-container');
    employeesContainer.innerHTML = ''

  fetch('http://localhost:8088/employees',requestOptions)
    .then(response => response.json())
    .then(json => json.forEach(employee => {

      let employeeRow = document.createElement('tr')

      let firstEmployeeIdCol = document.createElement('td')
      let secondEmployeeIdCol	= document.createElement('td')
      let daysWorkedCol	= document.createElement('td')

        firstEmployeeIdCol.textContent = employee.firstEmployeeId
        secondEmployeeIdCol.textContent = employee.secondEmployeeId
        daysWorkedCol.textContent = employee.daysWorkedId

        employeeRow.appendChild(firstEmployeeIdCol)
        employeeRow.appendChild(secondEmployeeIdCol)
        employeeRow.appendChild(daysWorkedCol)

        employeesContainer.append(employeeRow)
    }))
      .catch(error => console.log('error', error));

}
