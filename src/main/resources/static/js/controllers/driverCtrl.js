var app = angular.module('app').controller('driverCtrl', function ($scope, serviceAPI, $routeParams, $http, $cookieStore, AuthService, $window, $interval, http) {
  var url =  $cookieStore.get('URL');
  var defaultUrl = "marketplace.openbaton.org:80";
  $scope.drivers = [];
  $scope.alerts = [];


  function loadTable() {
      //console.log($routeParams.userId);
      $http.get("http://"+ defaultUrl + "/api/v1/vim-drivers")
          .success(function (response) {
              $scope.drivers = response;

              //console.log($scope.packages);
          })
          .error(function (data, status) {
              showError(data, status);
          });


  }

  $scope.install = function(data) {
    $scope.requestlink = {};
    $scope.requestlink['link'] = "http://" + defaultUrl + "/api/v1/vnf-packages/" + data.id + "/jar/";
    http.post(url + "/api/v1/vnf-packages/plugininstall", JSON.stringify($scope.requestlink)).success(function (response) {
     showOk("Plugin will be installed");
     })
    .error(function (data, status) {
        console.error('STATUS: ' + status + ' DATA: ' + JSON.stringify(data));

    });

  };


});
