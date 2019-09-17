var app = angular.module("CompanyManagement", ["xeditable"]);

app.run(['editableOptions', function(editableOptions) {
    editableOptions.theme = 'bs4'; // bootstrap3 theme. Can be also 'bs4', 'bs2', 'default'
}]);

app.controller('EditableFormCtrl', function($scope, $filter, $http) {

    $scope.saveUser = function() {
        // $scope.user already updated!
        /*return $http.post('/saveUser', $scope.user).error(function(err) {
            if(err.field && err.msg) {
                // err like {field: "name", msg: "Server-side error for this username!"}
                $scope.editableForm.$setError(err.field, err.msg);
            } else {
                // unknown error
                $scope.editableForm.$setError('name', 'Unknown error!');
            }
        });*/
        $http({
            method: "POST",
            url: '/company_edit',
            data: angular.toJson($scope.companyRest),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    function _success(res) {
        console.log("success");
    }

    function _error() {
        console.log("_error");
    }
});