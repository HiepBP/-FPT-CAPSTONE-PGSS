using AutoMapper.QueryableExtensions;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Sdk
{
    public partial class TransactionApi
    {
        public IEnumerable<TransactionCustomViewModel> GetTransactionByUserId(string userId)
        {
            var transactions = this.BaseService.GetTransactionByUserId(userId);
            var result = transactions.ProjectTo<TransactionCustomViewModel>(this.AutoMapperConfig).AsEnumerable();
            return result;
        }

        public TransactionViewModel CheckCode(string userId, string transactionCode, int carParkId)
        {
            var entity = this.BaseService.CheckCode(userId, transactionCode, carParkId);
            if(entity == null)
            {
                return null;
            }
            var model = new TransactionViewModel(entity);
            return model;
        }

        public TransactionViewModel CreateTransaction(TransactionViewModel model)
        {
            var entity = model.ToEntity();
            this.BaseService.Create(entity);
            model.Id = entity.Id;
            return model;
        }

        public void ChangeStatus(TransactionUpdateViewModel model)
        {
            var entity = this.BaseService.Get(model.Id);
            entity.Status = model.Status;
            this.BaseService.Update(entity);
        }
    }


}