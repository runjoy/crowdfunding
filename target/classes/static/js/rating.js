var cStars = function(nowPos) {
    $('.stars .star').removeClass('active');
    for (var i = 0; nowPos + 1 > i; i++) {
        $('.stars .star').eq(i).toggleClass('active');
    }
}

var starsCount = $('.star.active').length;

$('.stars .star').hover(function() {
    cStars($(this).index());
});

$('.stars .star').click(function() {
    cStars($(this).index());
    starsCount = $('.star.active').length;
});

$('.stars .star').on('mouseleave', function() {
    cStars(+starsCount - 1);
});


var app = angular.module("VoteManagement", []);
app.controller("VoteController", function($scope, $http) {

    $scope.companyRatingForm = {
        companyId: 0,
        vote: 0
    };

    $scope.addVote = function (vote) {
        $scope.companyRatingForm.companyId = $scope.company_id;
        $scope.companyRatingForm.vote = vote;
        $http({
            method: "POST",
            url: '/vote',
            data: angular.toJson($scope.companyRatingForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    }

    function _success() {
        console.log("success");
    }

    function _error() {
        console.log("_error");
    }
});